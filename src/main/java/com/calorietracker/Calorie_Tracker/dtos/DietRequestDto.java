package com.calorietracker.Calorie_Tracker.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public record DietRequestDto(
                @NotBlank String name,
                LocalDate initialDate,
                LocalDate finalDate) {
}
