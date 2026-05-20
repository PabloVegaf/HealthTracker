package com.healthtracker.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BodyMeasurementRequest(
        LocalDate measurementDate,
        BigDecimal chestCm,
        BigDecimal waistCm,
        BigDecimal hipsCm,
        BigDecimal armCm,
        BigDecimal thighCm,
        BigDecimal neckCm
) {
}
