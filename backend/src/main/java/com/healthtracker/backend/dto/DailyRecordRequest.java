package com.healthtracker.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO de entrada para crear o actualizar un registro diario.
 */
public record DailyRecordRequest(
        // Fecha del registro (por defecto suele ser hoy o ayer en frontend).
        LocalDate recordDate,
        // Peso diario en kg.
        BigDecimal weightKg,
        // Porcentaje de grasa corporal diario.
        BigDecimal bodyFatPct,
        // Kcal consumidas en el día.
        Integer kcalConsumed,
        // Kcal gastadas en el día.
        Integer kcalExpended,
        // Lista de sesiones de ejercicio de ese día.
        List<ExerciseSessionRequest> sessions,
        // Notas libres del usuario sobre el día.
        String notes
) {
}
