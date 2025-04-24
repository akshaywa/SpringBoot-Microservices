package com.dailycodebuffer.AuthService.service;

import io.jsonwebtoken.Claims;

import java.util.List;

public interface AuthService {
    String generateAccessToken(String userId, String email, List<String> roles);

    String generateRefreshToken(String userId, String email);

    Claims extractClaims(String token);

    boolean isTokenValid(String token);

    List<String> getUserRoles(String sub, String email);
}
