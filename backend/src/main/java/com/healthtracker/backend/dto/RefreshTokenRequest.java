package com.healthtracker.backend.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO usado para renovar sesión o cerrar sesión a partir de un refresh token.
 */
public record RefreshTokenRequest(
        @NotBlank
        String refreshToken
) {
}
