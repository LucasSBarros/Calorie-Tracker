package com.calorietracker.dtos.summary;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record DietSummaryResponse(
        UUID idDiet,
        String name,
        BigDecimal totalCalories,
        LocalDate initialDate,
        LocalDate finalDate,
        List<MealSummaryResponse> meals) {
}
