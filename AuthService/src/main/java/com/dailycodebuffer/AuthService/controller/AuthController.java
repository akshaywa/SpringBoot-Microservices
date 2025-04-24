package com.dailycodebuffer.AuthService.controller;

import com.dailycodebuffer.AuthService.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/token")
    public ResponseEntity<?> getAccessToken(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof DefaultOAuth2User oauthUser)) {
            return ResponseEntity.status(401).body("{\"error\": \"Unauthorized\"}");
        }

        String email = oauthUser.getAttribute("email");
        String userId = oauthUser.getAttribute("sub");
        List<String> roles = authService.getUserRoles(userId, email);

        String accessToken = authService.generateAccessToken(userId, email, roles);

        return ResponseEntity.ok().body("{\"token\": \"" + accessToken + "\"}");
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
        Optional<String> refreshTokenOpt = Arrays.stream(request.getCookies()).filter(cookie -> "refreshToken".equals(cookie.getName())).map(Cookie::getValue).findFirst();

        if (refreshTokenOpt.isEmpty()) {
            return ResponseEntity.status(403).body("{\"error\": \"Refresh token missing\"}");
        }

        String refreshToken = refreshTokenOpt.get();
        if (!authService.isTokenValid(refreshToken)) {
            return ResponseEntity.status(403).body("{\"error\": \"Invalid refresh token\"}");
        }

        String userId = (String) authService.extractClaims(refreshToken).get("userId");
        String email = authService.extractClaims(refreshToken).getSubject();
        List<String> roles = authService.getUserRoles(userId, email);

        String newAccessToken = authService.generateAccessToken(userId, email, roles);
        return ResponseEntity.ok().body("{\"token\": \"" + newAccessToken + "\"}");
    }


    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam("token") String token) {
        try {
            authService.isTokenValid(token);
            return new ResponseEntity<>("Token is valid", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Token is invalid", HttpStatus.UNAUTHORIZED);
        }
    }

}
