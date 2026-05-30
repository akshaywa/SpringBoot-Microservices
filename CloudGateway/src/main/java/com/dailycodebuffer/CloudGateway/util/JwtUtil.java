package com.dailycodebuffer.CloudGateway.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtUtil {

    private final String secret;
    private Key signingKey;

    public JwtUtil(@Value("${auth.jwt.secret:}") String secret) {
        this.secret = secret;
    }

    @PostConstruct
    public void init() {
        if (secret == null || secret.isBlank()) {
            // fallback insecure key - logically should be configured in prod
            signingKey = Keys.hmacShaKeyFor("change_this_to_a_secure_long_random_secret_key".getBytes(StandardCharsets.UTF_8));
        } else {
            signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }
    }

    public void validateToken(String token) {
        Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
    }
}
