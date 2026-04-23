package com.calorietracker.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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
@Table(name = "diets")
public class DietModel implements Serializable {

    public DietModel() {
        this.meals = new LinkedHashSet<>();
        this.totalCalories = BigDecimal.ZERO;
    }

    private static final long serialVersionUID = 1L;

    @Id
    @UuidGenerator
    private UUID idDiet; // ID

    private String name; // Name

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user; // Usuário

    @OneToMany(mappedBy = "diet", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MealModel> meals; // Refeições

    private BigDecimal totalCalories; // Total de calorias

    private LocalDate initialDate; // Data inicial

    private LocalDate finalDate; // Data final

    /**
     * Calcula o total de calorias da dieta com base nas refeições cadastradas.
     * 
     * @return total de calorias da dieta
     */
    public BigDecimal calculateTotalCalories() {

        BigDecimal result = BigDecimal.ZERO;

        if (meals != null) {
            for (MealModel meal : meals) {
                if (meal != null && meal.getTotalCaloriesPerMeal() != null) {
                    result = result.add(meal.getTotalCaloriesPerMeal());
                }
            }
        }

        return result;

    }

    /**
     * Atualiza o total de calorias da dieta com base nas refeições atuais.
     */
    public void updateTotalCalories() {
        this.totalCalories = calculateTotalCalories();
    }
}
