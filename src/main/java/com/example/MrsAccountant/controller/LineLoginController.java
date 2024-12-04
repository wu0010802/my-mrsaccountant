package com.example.mrsaccountant.controller;

import com.example.mrsaccountant.entity.User;
import com.example.mrsaccountant.repository.UserRepository;
import com.example.mrsaccountant.util.JwtUtil;
import com.example.mrsaccountant.util.LineSigningKeyResolver;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class LineLoginController {

    private final String profileUrl = "https://api.line.me/v2/profile";
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public LineLoginController(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/line")
    public ResponseEntity<?> lineLogin(@RequestParam String idToken) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKeyResolver(new LineSigningKeyResolver())
                .build()
                .parseClaimsJws(idToken)
                .getBody();

        String lineId = claims.get("aud", String.class);
        String displayName = claims.get("name", String.class);

        User user = userRepository.findByLineId(lineId).orElse(new User());
        user.setLineId(lineId);
        user.setUsername(displayName);

        userRepository.save(user);

        String jwt = jwtUtil.generateToken(user.getUserId().toString());

        return ResponseEntity.ok(Map.of("jwt", jwt, "userId", user.getUsername()));
    }
}
