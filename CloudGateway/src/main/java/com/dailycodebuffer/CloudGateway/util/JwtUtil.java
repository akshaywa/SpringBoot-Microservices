package com.dailycodebuffer.CloudGateway.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
@RefreshScope
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    private final String secret;
    private Key signingKey;

    public JwtUtil(@Value("${auth.jwt.secret:}") String secret) {
        this.secret = secret;
    }

    @PostConstruct
    public void init() {
        if (secret == null || secret.isBlank()) {
            log.warn("JWT secret not provided via Config Server; using fallback insecure key for local/dev only.");
            signingKey = Keys.hmacShaKeyFor("change_this_to_a_secure_long_random_secret_key".getBytes(StandardCharsets.UTF_8));
        } else {
            signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }
    }

    public void validateToken(String token) {
        if (token == null || token.isBlank()) throw new IllegalArgumentException("token must not be empty");
        Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
    }
}
