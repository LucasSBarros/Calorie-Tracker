package com.calorietracker.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
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
    private LinkedHashSet<MealIngredientModel> mealIngredients; // Ingredientes

    private BigDecimal totalCaloriesPerMeal; // Valor total das calorias de uma refeição

    @ManyToOne
    @JoinColumn(name = "diet_id")
    private DietModel diet; // Dieta
}
