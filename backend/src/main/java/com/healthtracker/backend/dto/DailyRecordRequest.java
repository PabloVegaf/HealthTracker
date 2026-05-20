package com.healthtracker.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record DailyRecordRequest(
        LocalDate recordDate,
        BigDecimal weightKg,
        BigDecimal bodyFatPct,
        Integer kcalConsumed,
        Integer kcalExpended,
        List<ExerciseSessionRequest> sessions,
        String notes
) {
}
