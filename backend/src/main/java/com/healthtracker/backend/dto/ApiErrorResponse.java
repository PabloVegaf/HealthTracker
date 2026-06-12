package com.healthtracker.backend.dto;

import java.time.Instant;

/**
 * Formato común de error para que la API no devuelva trazas internas.
 */
public record ApiErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
