package com.healthtracker.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada para registro de usuario.
 */
public record RegisterRequest(
        // Email único del usuario.
        @NotBlank
        @Email
        String email,
        // Contraseña elegida por el usuario.
        @NotBlank
        @Size(min = 8, max = 72)
        String password,
        // Nombre visible del usuario.
        @NotBlank
        @Size(max = 100)
        String name
) {
}
