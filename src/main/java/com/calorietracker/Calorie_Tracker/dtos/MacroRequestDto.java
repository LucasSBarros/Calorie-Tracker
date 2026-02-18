package com.calorietracker.Calorie_Tracker.dtos;

import java.math.BigDecimal;

public record MacroRequestDto(
        BigDecimal carb,
        BigDecimal protein,
        BigDecimal fat,
        BigDecimal calories) {
}