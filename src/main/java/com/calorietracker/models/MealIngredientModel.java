package com.calorietracker.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    /**
     * Método para Cálculo de Calorias por 100g
     * 
     * @return
     */
    public BigDecimal calculateCalories() {

        if (ingredient == null || ingredient.getMacro() == null || weight == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal caloriesPer100g = ingredient.getMacro().getCalories();

        if (caloriesPer100g == null) {
            return BigDecimal.ZERO;
        }

        return caloriesPer100g
                .multiply(weight)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

}
