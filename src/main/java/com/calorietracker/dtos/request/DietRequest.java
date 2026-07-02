package com.calorietracker.dtos.request;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DietRequest(
        @NotBlank String name,
        @NotNull UUID userId,
        LocalDate initialDate,
        LocalDate finalDate) {
}
