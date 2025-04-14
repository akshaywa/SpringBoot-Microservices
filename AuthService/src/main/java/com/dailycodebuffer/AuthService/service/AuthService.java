package com.dailycodebuffer.AuthService.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService {
    private static final String SECRET_KEY = "your_secret_key"; // Replace with a secure key
    private static final long ACCESS_TOKEN_EXPIRY = 1000 * 60 * 15; // 15 min
    private static final long REFRESH_TOKEN_EXPIRY = 1000 * 60 * 60 * 24 * 7; // 7 days

    public String generateAccessToken(String userId, String email) {
        return createToken(userId, email, ACCESS_TOKEN_EXPIRY);
    }

    public String generateRefreshToken(String userId, String email) {
        return createToken(userId, email, REFRESH_TOKEN_EXPIRY);
    }

    private String createToken(String userId, String subject, long expirationTime) {
        return Jwts.builder().setSubject(subject).claim("userId", userId) // Store user ID
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

