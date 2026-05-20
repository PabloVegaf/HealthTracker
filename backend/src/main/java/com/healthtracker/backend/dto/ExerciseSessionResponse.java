package com.healthtracker.backend.dto;

public record ExerciseSessionResponse(
        Long exerciseCategoryId,
        String exerciseCategoryName,
        Integer durationMin,
        String notes
) {}
