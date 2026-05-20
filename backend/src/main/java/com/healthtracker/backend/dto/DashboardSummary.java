package com.healthtracker.backend.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record DashboardSummary(
        String currentWeight,
        String currentBodyFat,
        String weightChange7d,
        String bodyFatChange7d,
        String avgKcalConsumed7d,
        String avgKcalExpended7d,
        List<Map<String, Object>> weeklySummary
) {
}
