package com.healthtracker.backend.dto;

/**
 * DTO de entrada para crear o editar una sesión de ejercicio dentro de un registro diario.
 */
public record ExerciseSessionRequest(
        // ID de la categoría de ejercicio seleccionada por el usuario (ej: correr, natación).
        Long exerciseCategoryId,
        // Duración de la sesión en minutos.
        Integer durationMin,
        // Notas opcionales sobre la sesión (intensidad, sensaciones, etc.).
        String notes
) {}
