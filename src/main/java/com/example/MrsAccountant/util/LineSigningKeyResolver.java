package com.example.mrsaccountant.util;

import io.jsonwebtoken.SigningKeyResolver;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Claims;

import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.PublicKey;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;


import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LineSigningKeyResolver implements SigningKeyResolver {

    private static final String LINE_JWKS_URL = "https://api.line.me/oauth2/v2.1/certs";

  
    private final Map<String, PublicKey> publicKeyCache = new ConcurrentHashMap<>();

    @Override
    public Key resolveSigningKey(JwsHeader header, Claims claims) {
        String kid = header.getKeyId();
        return getKeyFromKid(kid);
    }

    @Override
    public Key resolveSigningKey(JwsHeader header, String plaintext) {
        String kid = header.getKeyId();
        return getKeyFromKid(kid);
    }

    private PublicKey getKeyFromKid(String kid) {
        if (publicKeyCache.containsKey(kid)) {
            return publicKeyCache.get(kid);
        }

        try {
            
            String jwksResponse = new RestTemplate().getForObject(LINE_JWKS_URL, String.class);
            Map<String, Object> jwks = new ObjectMapper().readValue(jwksResponse, Map.class);
            List<Map<String, Object>> keys = (List<Map<String, Object>>) jwks.get("keys");

            for (Map<String, Object> key : keys) {
                if (kid.equals(key.get("kid"))) {
                    String kty = (String) key.get("kty");
                    if ("EC".equals(kty)) {
                     
                        String crv = (String) key.get("crv");
                        String x = (String) key.get("x");
                        String y = (String) key.get("y");

                        if (crv == null || x == null || y == null) {
                            throw new RuntimeException("Invalid EC key data for kid: " + kid);
                        }

              
                        return generateEcPublicKey(crv, x, y);
                    } else {
                        throw new RuntimeException("Unsupported key type: " + kty);
                    }
                }
            }

            throw new RuntimeException("Key not found for kid: " + kid);

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch or parse public key: " + e.getMessage(), e);
        }
    }

    private PublicKey generateEcPublicKey(String crv, String x, String y) throws Exception {
      
        if (!"P-256".equals(crv)) {
            throw new IllegalArgumentException("Unsupported curve: " + crv);
        }


        byte[] xBytes = Base64.getUrlDecoder().decode(x);
        byte[] yBytes = Base64.getUrlDecoder().decode(y);


        ECPoint ecPoint = new ECPoint(new BigInteger(1, xBytes), new BigInteger(1, yBytes));
        AlgorithmParameters parameters = AlgorithmParameters.getInstance("EC");
        parameters.init(new ECGenParameterSpec("secp256r1"));
        ECParameterSpec ecParameterSpec = parameters.getParameterSpec(ECParameterSpec.class);

        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        ECPublicKeySpec publicKeySpec = new ECPublicKeySpec(ecPoint, ecParameterSpec);
        return keyFactory.generatePublic(publicKeySpec);
    }

}
