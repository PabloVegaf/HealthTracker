package com.healthtracker.backend.dto;

/**
 * Respuesta pública de usuario. Nunca debe incluir passwordHash ni otros secretos.
 */
public record UserResponse(
        Long id,
        String email,
        String name
) {
}
