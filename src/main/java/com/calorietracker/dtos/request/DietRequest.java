package com.calorietracker.dtos.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DietRequest(
        @NotBlank String name,
        @NotNull UUID userId,
        @NotNull @Positive BigDecimal dailyCalorieTarget,
        LocalDate initialDate,
        LocalDate finalDate) {
}
