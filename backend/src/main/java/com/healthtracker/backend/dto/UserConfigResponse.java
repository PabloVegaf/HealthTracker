package com.healthtracker.backend.dto;

/**
 * DTO de salida para exponer configuración de proveedor IA sin secretos.
 */
public record UserConfigResponse(
        // Proveedor seleccionado (OpenAI, Ollama, etc.).
        String provider,
        // URL base compatible con OpenAI API.
        String baseUrl,
        // Modelo por defecto usado en el chat.
        String defaultModel
) {
}
