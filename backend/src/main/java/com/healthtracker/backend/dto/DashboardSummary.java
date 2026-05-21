package com.healthtracker.backend.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * DTO agregado para el resumen principal del dashboard.
 */
public record DashboardSummary(
        // Peso actual mostrado al usuario.
        String currentWeight,
        // Porcentaje graso actual mostrado al usuario.
        String currentBodyFat,
        // Cambio de peso de los últimos 7 días.
        String weightChange7d,
        // Cambio de grasa corporal de los últimos 7 días.
        String bodyFatChange7d,
        // Promedio de kcal ingeridas en 7 días.
        String avgKcalConsumed7d,
        // Promedio de kcal gastadas en 7 días.
        String avgKcalExpended7d,
        // Estructura semanal para gráficos y resúmenes.
        List<Map<String, Object>> weeklySummary
) {
}
