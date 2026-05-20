package com.healthtracker.backend.dto;

public record RegisterRequest(
        String email,
        String password,
        String name
) {
}
