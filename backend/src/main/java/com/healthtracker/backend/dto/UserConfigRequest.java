package com.healthtracker.backend.dto;

/**
 * DTO de entrada para actualizar configuración del proveedor IA del usuario.
 */
public record UserConfigRequest(
        // Nombre del proveedor IA.
        String provider,
        // URL base del proveedor (OpenAI-compatible).
        String baseUrl,
        // Modelo por defecto a utilizar.
        String defaultModel,
        // API key del proveedor (solo backend; nunca se devuelve al frontend).
        String apiKey
) {
}
