package com.calorietracker.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record UserProgressDto(
                UUID userId,
                BigDecimal currentWeight,
                BigDecimal goalWeight,
                BigDecimal weightProgressPercent,
                BigDecimal currentBf,
                BigDecimal goalBf,
                BigDecimal bfProgressPercent) {
}