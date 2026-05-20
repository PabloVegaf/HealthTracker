package com.healthtracker.backend.dto;

public record ExerciseSessionRequest(
        Long exerciseCategoryId,
        Integer durationMin,
        String notes
) {}
