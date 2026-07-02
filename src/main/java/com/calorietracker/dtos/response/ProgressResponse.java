package com.calorietracker.dtos.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ProgressResponse(
        UUID userId,
        BigDecimal currentWeight,
        BigDecimal goalWeight,
        BigDecimal weightProgressPercent,
        BigDecimal currentBf,
        BigDecimal goalBf,
        BigDecimal bfProgressPercent) {
}
