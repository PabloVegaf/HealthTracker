package com.healthtracker.backend.dto;

/**
 * DTO de salida para respuestas de autenticación JWT.
 */
public record AuthResponse(
        // Access token de corta duración.
        String accessToken,
        // Refresh token para renovar sesión.
        String refreshToken,
        // Tipo de esquema de autorización (Bearer).
        String tokenType,
        // Segundos hasta expiración del access token.
        long expiresIn
) {
    public AuthResponse(String accessToken, String refreshToken, long expiresIn) {
        this(accessToken, refreshToken, "Bearer", expiresIn);
    }
}
