package com.dailycodebuffer.AuthService.service;

import com.dailycodebuffer.AuthService.entity.User;
import com.dailycodebuffer.AuthService.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RefreshScope
public class AuthServiceImpl implements AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;

    private final String secretKey;
    private Key signingKey;

    private static final long ACCESS_TOKEN_EXPIRY = 1000 * 60 * 15; // 15 min
    private static final long REFRESH_TOKEN_EXPIRY = 1000 * 60 * 60 * 24 * 7; // 7 days

    public AuthServiceImpl(UserRepository userRepository, @Value("${auth.jwt.secret:}") String secretKey) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.secretKey = secretKey;
    }

    @PostConstruct
    private void init() {
        if (secretKey == null || secretKey.isBlank()) {
            log.warn("JWT secret key is not set via configuration. Using fallback insecure key - update application properties for production.");
            signingKey = Keys.hmacShaKeyFor("change_this_to_a_secure_long_random_secret_key".getBytes(StandardCharsets.UTF_8));
        } else {
            signingKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        }
    }

    @Override
    public String generateAccessToken(String userId, String email, List<String> roles) {
        return createToken(userId, email, roles, ACCESS_TOKEN_EXPIRY);
    }

    @Override
    public String generateRefreshToken(String userId, String email) {
        return createRefreshToken(userId, email, REFRESH_TOKEN_EXPIRY);
    }

    private String createToken(String userId, String email, List<String> roles, long expirationTime) {
        return Jwts.builder()
                .setSubject(userId)
                .claim("email", email)
                .claim("roles", roles)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private String createRefreshToken(String userId, String email, long expirationTime) {
        return Jwts.builder()
                .setSubject(userId)
                .claim("email", email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public Claims extractClaims(String token) {
        if (token == null) throw new IllegalArgumentException("token must not be null");
        return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            log.debug("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public List<String> getUserRoles(String sub, String email) {
        if (sub == null || sub.isBlank()) throw new IllegalArgumentException("user id cannot be null");
        User user = userRepository.findById(sub).orElseGet(() -> {
            User newUser = User.builder().userId(sub)
                    .email(email).roles(List.of("ROLE_USER")).build();
            return userRepository.save(newUser);
        });
        return user.getRoles();
    }
}

