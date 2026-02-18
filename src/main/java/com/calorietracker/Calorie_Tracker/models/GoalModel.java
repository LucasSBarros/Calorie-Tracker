package com.calorietracker.Calorie_Tracker.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "goals")
public class GoalModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @UuidGenerator
    private UUID idGoal; // ID

    private BigDecimal weight; // Peso

    private BigDecimal imc; // Índice de Massa Corporal

    private BigDecimal bf; // Gordura corporal

    private BigDecimal tmb; // Taxa Metabólica Basal

}
