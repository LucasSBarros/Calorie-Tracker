package com.calorietracker.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record DietResponse(
        UUID idDiet,
        String name,
        List<MealResponse> meals,
        BigDecimal totalCalories,
        BigDecimal dailyCalorieTarget,
        LocalDate initialDate,
        LocalDate finalDate) {
}
