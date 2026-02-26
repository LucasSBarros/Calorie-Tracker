package com.calorietracker.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record MacroDto(
                UUID idMacro,
                BigDecimal carb,
                BigDecimal protein,
                BigDecimal fat,
                BigDecimal calories) {
}