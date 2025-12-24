// JwtUtil.java--用于生成和解析JWT令牌
package com.example.app.security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {
    private final String secret = "ChangeThisSecretForProd";// 在生产环境中请使用更安全的密钥
    private final long expireMs = 1000L * 60 * 60 * 24;// 24小时有效期

    // 生成JWT令牌
    public String generateToken(Long userId, String username) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("username", username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireMs))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    // 解析JWT令牌
    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}