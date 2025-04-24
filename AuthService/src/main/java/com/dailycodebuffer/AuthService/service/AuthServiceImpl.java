package com.dailycodebuffer.AuthService.service;

import com.dailycodebuffer.AuthService.entity.User;
import com.dailycodebuffer.AuthService.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    private static final String SECRET_KEY = "your_secret_key"; // Replace with a secure key
    private static final long ACCESS_TOKEN_EXPIRY = 1000 * 60 * 15; // 15 min
    private static final long REFRESH_TOKEN_EXPIRY = 1000 * 60 * 60 * 24 * 7; // 7 days
    @Autowired
    private UserRepository userRepository;

    public String generateAccessToken(String userId, String email, List<String> roles) {
        return createToken(userId, email, roles, ACCESS_TOKEN_EXPIRY);
    }

    public String generateRefreshToken(String userId, String email) {
        return createRefreshToken(userId, email, REFRESH_TOKEN_EXPIRY);
    }

    private String createToken(String userId, String email, List<String> roles, long expirationTime) {
        return Jwts.builder().setSubject(userId).claim("email", email) // Store user ID
                .claim("roles", roles).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + expirationTime)).signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    private String createRefreshToken(String userId, String email, long expirationTime) {
        return Jwts.builder().setSubject(userId).claim("email", email) // Store user ID
                .setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + expirationTime)).signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
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

    public List<String> getUserRoles(String sub, String email) {
        User user = userRepository.findById(sub).orElseGet(() -> {
            User newUser = User.builder().userId(sub)     // <-- Use Google's sub
                    .email(email).roles(List.of("ROLE_USER")).build();
            return userRepository.save(newUser);
        });
        return user.getRoles();
    }
}

