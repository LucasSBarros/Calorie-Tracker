package com.calorietracker.dtos.request;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record GoalRequest(
        @NotNull UUID userId,
        @NotNull @PositiveOrZero BigDecimal weight,
        @NotNull @PositiveOrZero BigDecimal startWeight,
        BigDecimal bf,
        BigDecimal startBf) {
}
