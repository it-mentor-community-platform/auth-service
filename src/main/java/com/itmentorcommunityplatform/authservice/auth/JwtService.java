package com.itmentorcommunityplatform.authservice.auth;

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

    public String generateToken(Long telegramUserId, List<String> roles) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiration = new Date(now + expirationMillis);
        return Jwts.builder()
                .subject(String.valueOf(telegramUserId))
                .claim("roles", roles).issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }
}
