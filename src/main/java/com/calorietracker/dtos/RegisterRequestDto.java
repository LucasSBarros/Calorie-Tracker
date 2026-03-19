package com.calorietracker.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.calorietracker.models.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;

public record RegisterRequestDto(
                @NotBlank String name,
                @Email @NotBlank String email,
                @NotBlank String password,
                @NotNull @Positive BigDecimal weight,
                @NotNull @Positive BigDecimal height,
                @NotNull @Past LocalDate birthDate,
                @NotNull Gender gender) {
}