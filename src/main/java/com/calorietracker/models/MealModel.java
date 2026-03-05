package com.calorietracker.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "meals")
public class MealModel implements Serializable {

    public MealModel() {
        this.mealIngredients = new LinkedHashSet<>();
        this.totalCaloriesPerMeal = BigDecimal.ZERO;
    }

    private static final long serialVersionUID = 1L;

    @Id
    @UuidGenerator
    private UUID idMeal; // ID

    private LocalDateTime mealDateTime; // Horário da Refeição

    private String description; // Descrição da Refeição

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MealIngredientModel> mealIngredients; // Ingredientes

    private BigDecimal totalCaloriesPerMeal; // Valor total das calorias de uma refeição

    @ManyToOne
    @JoinColumn(name = "diet_id")
    private DietModel diet; // Dieta

    /**
     * Método para Cálculo do total de calorias por refeição
     * 
     * @return
     */
    public BigDecimal calculateTotalCaloriesPerMeal() {

        if (mealIngredients == null || mealIngredients.isEmpty()) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal total = BigDecimal.ZERO;

        for (MealIngredientModel mealIngredient : mealIngredients) {
            total = total.add(mealIngredient.calculateCalories());
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }

}
