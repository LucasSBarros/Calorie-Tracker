package com.calorietracker.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record StatusRequestDto(
                @NotNull @PositiveOrZero BigDecimal weight,
                BigDecimal bf) {
}