package com.example.mrsaccountant.controller;

import com.example.mrsaccountant.entity.User;
import com.example.mrsaccountant.repository.UserRepository;
import com.example.mrsaccountant.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
                .build()
                .parseClaimsJwt(idToken.substring(0, idToken.lastIndexOf('.') + 1)) // 不驗證簽名
                .getBody();

        String lineId =claims.get("aud", String.class);
        String displayName = claims.get("name", String.class);

        // 查詢或創建用戶
        User user = userRepository.findByLineId(lineId).orElse(new User());
        user.setLineId(lineId);
        user.setUsername(displayName);

        userRepository.save(user);

        // 生成 JWT Token
        String jwt = jwtUtil.generateToken(user.getUserId().toString());

        // 返回響應
        return ResponseEntity.ok(Map.of("jwt", jwt, "userId", user.getUsername()));
    }
}
