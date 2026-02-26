package com.calorietracker.dtos;

import java.math.BigDecimal;

public record MacroRequestDto(
        BigDecimal carb,
        BigDecimal protein,
        BigDecimal fat,
        BigDecimal calories) {
}