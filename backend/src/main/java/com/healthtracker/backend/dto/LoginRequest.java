package com.healthtracker.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO de entrada para login con email y contraseña.
 */
public record LoginRequest(
        // Email del usuario.
        @NotBlank
        @Email
        String email,
        // Contraseña en texto plano (solo en tránsito HTTPS).
        @NotBlank
        String password
) {
}
