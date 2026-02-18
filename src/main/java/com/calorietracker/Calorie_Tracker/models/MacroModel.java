package com.calorietracker.Calorie_Tracker.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "macros")
public class MacroModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @UuidGenerator
    private UUID idMacro;

    @NotNull
    @PositiveOrZero
    private BigDecimal carb; // Carboidráto

    @NotNull
    @PositiveOrZero
    private BigDecimal protein; // Proteína

    @NotNull
    @PositiveOrZero
    private BigDecimal fat; // Gordura

    @NotNull
    @PositiveOrZero
    private BigDecimal calories; // Calorias

}
