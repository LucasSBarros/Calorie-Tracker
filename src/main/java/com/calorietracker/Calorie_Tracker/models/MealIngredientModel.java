package com.calorietracker.Calorie_Tracker.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "meal_ingredients")
public class MealIngredientModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @UuidGenerator
    private UUID idMealIngredient; // ID

    private BigDecimal weight; // Peso

    @ManyToOne
    @JoinColumn(name = "meal_id", nullable = false)
    private MealModel meal; // Refeição

    @ManyToOne
    @JoinColumn(name = "ingredient_id", nullable = false)
    private IngredientModel ingredient; // Ingrediente

}
