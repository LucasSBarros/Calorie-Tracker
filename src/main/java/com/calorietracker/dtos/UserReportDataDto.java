package com.calorietracker.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.calorietracker.models.Gender;

public record UserReportDataDto(
                UUID userId,
                String name,
                String email,
                BigDecimal weight,
                BigDecimal height,
                LocalDate birthDate,
                int age,
                Gender gender,
                BigDecimal imc,
                BigDecimal tmb,

                BigDecimal goalWeight,
                BigDecimal goalBf,

                BigDecimal currentWeight,
                BigDecimal currentBf,

                BigDecimal weightProgressPercent,
                BigDecimal bfProgressPercent,

                List<DietReportDto> diets) {
}