package com.healthtracker.backend.dto;

/**
 * DTO de entrada para login con email y contraseña.
 */
public record LoginRequest(
        // Email del usuario.
        String email,
        // Contraseña en texto plano (solo en tránsito HTTPS).
        String password
) {
}
