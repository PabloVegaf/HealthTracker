package com.healthtracker.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO de salida de un registro diario completo, listo para pintar en UI.
 */
public record DailyRecordResponse(
        // ID interno del registro diario.
        Long id,
        // Fecha del registro.
        LocalDate recordDate,
        // Peso en kg para la fecha.
        BigDecimal weightKg,
        // Porcentaje de grasa corporal.
        BigDecimal bodyFatPct,
        // Kcal ingeridas en el día.
        Integer kcalConsumed,
        // Kcal gastadas en el día.
        Integer kcalExpended,
        // Sesiones de ejercicio del día.
        List<ExerciseSessionResponse> sessions,
        // Duración total (min) calculada desde las sesiones.
        Integer exerciseDurationMin,
        // Notas generales del día.
        String notes
) {
}
