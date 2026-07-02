package com.calorietracker.dtos.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record UserReportResponse(
        UUID userId,
        String name,
        String email,
        BigDecimal weight,
        BigDecimal height,
        LocalDate birthDate,
        int age,
        String gender,
        BigDecimal imc,
        BigDecimal tmb,
        BigDecimal goalWeight,
        BigDecimal goalBf,
        BigDecimal currentWeight,
        BigDecimal currentBf,
        BigDecimal weightProgressPercent,
        BigDecimal bfProgressPercent,
        List<String> diets) {
}
