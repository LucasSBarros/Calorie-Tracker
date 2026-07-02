package com.calorietracker.dtos.request;

import java.math.BigDecimal;

public record MacroRequest(
        BigDecimal carb,
        BigDecimal protein,
        BigDecimal fat,
        BigDecimal calories) {
}
