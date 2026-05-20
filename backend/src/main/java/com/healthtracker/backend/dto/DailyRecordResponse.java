package com.healthtracker.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record DailyRecordResponse(
        Long id,
        LocalDate recordDate,
        BigDecimal weightKg,
        BigDecimal bodyFatPct,
        Integer kcalConsumed,
        Integer kcalExpended,
        List<ExerciseSessionResponse> sessions,
        Integer exerciseDurationMin,
        String notes
) {
}
