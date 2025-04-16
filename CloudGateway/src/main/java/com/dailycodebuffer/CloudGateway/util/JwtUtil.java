package com.dailycodebuffer.CloudGateway.util;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final String secret = "your_secret_key"; // same as in AuthService

    public void validateToken(String token) {
        Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
    }
}
