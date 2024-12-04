import io.jsonwebtoken.SigningKeyResolver;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Claims;

import java.security.Key;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LineSigningKeyResolver implements SigningKeyResolver {

    private static final String LINE_JWKS_URL = "https://access.line.me/.well-known/jwks.json";

    // 緩存公鑰
    private final Map<String, PublicKey> publicKeyCache = new ConcurrentHashMap<>();

    @Override
    public Key resolveSigningKey(JwsHeader header, Claims claims) {
        String kid = header.getKeyId(); // 從 Header 中提取 key ID
        return getKeyFromKid(kid);
    }

    @Override
    public Key resolveSigningKey(JwsHeader header, String plaintext) {
        String kid = header.getKeyId(); // 從 Header 中提取 key ID
        return getKeyFromKid(kid);
    }

    // 獲取公鑰的方法
    private PublicKey getKeyFromKid(String kid) {
        // 如果緩存中有，直接返回
        if (publicKeyCache.containsKey(kid)) {
            return publicKeyCache.get(kid);
        }

        try {
            // 請求 LINE JWKS JSON
            String jwksResponse = new RestTemplate().getForObject(LINE_JWKS_URL, String.class);

            // 解析 JWKS JSON
            Map<String, Object> jwks = new ObjectMapper().readValue(jwksResponse, Map.class);
            List<Map<String, Object>> keys = (List<Map<String, Object>>) jwks.get("keys");

            for (Map<String, Object> key : keys) {
                if (kid.equals(key.get("kid"))) {
                    // 提取 x5c（證書）
                    String x5c = ((List<String>) key.get("x5c")).get(0);
                    byte[] decodedKey = Base64.getDecoder().decode(x5c);

                    // 使用 CertificateFactory 生成 PublicKey
                    CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                    X509Certificate certificate = (X509Certificate) certificateFactory
                            .generateCertificate(new java.io.ByteArrayInputStream(decodedKey));
                    PublicKey publicKey = certificate.getPublicKey();

                    // 緩存公鑰
                    publicKeyCache.put(kid, publicKey);
                    return publicKey;
                }
            }

            throw new RuntimeException("Key not found for kid: " + kid);

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch or parse public key: " + e.getMessage(), e);
        }
    }
}
