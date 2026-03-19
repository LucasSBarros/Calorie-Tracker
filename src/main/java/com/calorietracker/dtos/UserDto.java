package com.calorietracker.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.calorietracker.models.Gender;

public record UserDto(
        UUID idUser,
        String name,
        String email,
        BigDecimal weight,
        BigDecimal height,
        LocalDate birthDate,
        int age,
        Gender gender,
        BigDecimal imc,
        BigDecimal tmb) {
}