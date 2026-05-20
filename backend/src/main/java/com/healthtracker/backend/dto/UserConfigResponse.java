package com.healthtracker.backend.dto;

public record UserConfigResponse(
        String provider,
        String baseUrl,
        String defaultModel
) {
}
