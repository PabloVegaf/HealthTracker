package com.healthtracker.backend.dto;

/**
 * DTO de entrada para registro de usuario.
 */
public record RegisterRequest(
        // Email único del usuario.
        String email,
        // Contraseña elegida por el usuario.
        String password,
        // Nombre visible del usuario.
        String name
) {
}
