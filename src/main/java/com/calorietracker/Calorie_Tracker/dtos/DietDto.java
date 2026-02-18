package com.calorietracker.Calorie_Tracker.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record DietDto(
                UUID idDiet,
                String name,
                Set<MealDto> meals,
                BigDecimal totalCalories,
                LocalDate initialDate,
                LocalDate finalDate) {
}