package com.healthtracker.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de entrada para crear una medición corporal.
 */
public record BodyMeasurementRequest(
        // Fecha de la medición.
        LocalDate measurementDate,
        // Contorno de pecho en centímetros.
        BigDecimal chestCm,
        // Contorno de cintura en centímetros.
        BigDecimal waistCm,
        // Contorno de cadera en centímetros.
        BigDecimal hipsCm,
        // Contorno de brazo en centímetros.
        BigDecimal armCm,
        // Contorno de muslo en centímetros.
        BigDecimal thighCm,
        // Contorno de cuello en centímetros.
        BigDecimal neckCm
) {
}
