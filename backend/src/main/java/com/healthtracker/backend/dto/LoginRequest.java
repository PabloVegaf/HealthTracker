package com.healthtracker.backend.dto;

public record LoginRequest(
        String email,
        String password
) {
}
