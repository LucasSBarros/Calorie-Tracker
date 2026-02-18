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
@Table(name = "status")
public class StatusModel implements Serializable {

    @Id
    @UuidGenerator
    private UUID idStatus; // ID

    @NotNull
    @PositiveOrZero
    private BigDecimal weight; // Peso

    private BigDecimal imc; // Índice de Massa Corporal

    private BigDecimal bf; // Gordura corporal

    private BigDecimal tmb; // Taxa Metabólica Basal

}
