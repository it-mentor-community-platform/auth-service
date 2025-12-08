package com.itmentorcommunityplatform.authservice.auth;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private final Key key;
    private final long expirationMillis;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration_minutes}") long expirationMillis) {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.expirationMillis = expirationMillis * 60 * 1000;
    }

    public String generateToken(Long userId, List<String> roles, String telegramUsername) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiration = new Date(now + expirationMillis);

        JwtBuilder builder = Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("roles", roles)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(key);

        if (telegramUsername != null && !telegramUsername.trim().isEmpty()) {
            builder.claim("telegram_username", telegramUsername);
        }

        return builder.compact();
    }

    public String generateToken(Long userId, List<String> roles) {
        return generateToken(userId, roles, null);
    }
}
