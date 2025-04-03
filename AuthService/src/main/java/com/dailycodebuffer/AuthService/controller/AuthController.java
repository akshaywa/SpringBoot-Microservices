package com.dailycodebuffer.AuthService.controller;

import com.dailycodebuffer.AuthService.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/token")
    public ResponseEntity<?> getAccessToken(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof DefaultOAuth2User oauthUser)) {
            return ResponseEntity.status(401).body("{\"error\": \"Unauthorized\"}");
        }

        String email = oauthUser.getAttribute("email");
        String userId = oauthUser.getAttribute("sub");

        String accessToken = jwtUtil.generateAccessToken(userId, email);

        return ResponseEntity.ok().body("{\"token\": \"" + accessToken + "\"}");
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
        Optional<String> refreshTokenOpt = Arrays.stream(request.getCookies()).filter(cookie -> "refreshToken".equals(cookie.getName())).map(Cookie::getValue).findFirst();

        if (refreshTokenOpt.isEmpty()) {
            return ResponseEntity.status(403).body("{\"error\": \"Refresh token missing\"}");
        }

        String refreshToken = refreshTokenOpt.get();
        if (!jwtUtil.isTokenValid(refreshToken)) {
            return ResponseEntity.status(403).body("{\"error\": \"Invalid refresh token\"}");
        }

        String userId = (String) jwtUtil.extractClaims(refreshToken).get("userId");
        String email = jwtUtil.extractClaims(refreshToken).getSubject();

        String newAccessToken = jwtUtil.generateAccessToken(userId, email);
        return ResponseEntity.ok().body("{\"token\": \"" + newAccessToken + "\"}");
    }
}
