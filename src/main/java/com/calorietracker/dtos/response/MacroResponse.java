package com.calorietracker.dtos.response;

import java.math.BigDecimal;
import java.util.UUID;

public record MacroResponse(
        UUID idMacro,
        BigDecimal carb,
        BigDecimal protein,
        BigDecimal fat,
        BigDecimal calories) {
}
