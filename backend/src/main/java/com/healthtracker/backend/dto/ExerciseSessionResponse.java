package com.healthtracker.backend.dto;

/**
 * DTO de salida de una sesión de ejercicio, con datos ya enriquecidos para frontend.
 */
public record ExerciseSessionResponse(
        // ID de la categoría de ejercicio asociada.
        Long exerciseCategoryId,
        // Nombre de la categoría resuelto por backend (ej: "Correr").
        String exerciseCategoryName,
        // Duración total de la sesión en minutos.
        Integer durationMin,
        // Notas opcionales guardadas para la sesión.
        String notes
) {}
