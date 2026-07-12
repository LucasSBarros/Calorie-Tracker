package com.calorietracker.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Armazena um ingrediente e sua contribuição calórica em uma refeição
 * consumida.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "meal_log_items")
public class MealLogItemModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @UuidGenerator
    private UUID idMealLogItem;

    @ManyToOne(optional = false)
    @JoinColumn(name = "meal_log_id", nullable = false)
    private MealLogModel mealLog;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private IngredientModel ingredient;

    @Column(nullable = false)
    private BigDecimal weight;

    @Column(nullable = false)
    private BigDecimal calories;
}
