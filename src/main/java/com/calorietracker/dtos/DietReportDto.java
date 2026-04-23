package com.calorietracker.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record DietReportDto(
        UUID idDiet,
        String name,
        BigDecimal totalCalories,
        LocalDate initialDate,
        LocalDate finalDate,
        List<MealReportDto> meals) {
}