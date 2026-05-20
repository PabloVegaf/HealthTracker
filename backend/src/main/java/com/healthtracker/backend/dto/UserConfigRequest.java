package com.healthtracker.backend.dto;

public record UserConfigRequest(
        String provider,
        String baseUrl,
        String defaultModel,
        String apiKey
) {
}
