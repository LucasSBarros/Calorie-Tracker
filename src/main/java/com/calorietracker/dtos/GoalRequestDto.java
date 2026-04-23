package com.calorietracker.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record GoalRequestDto(
        @NotNull UUID userId,
        @NotNull @PositiveOrZero BigDecimal weight,
        @NotNull @PositiveOrZero BigDecimal startWeight,
        BigDecimal bf,
        BigDecimal startBf) {
}