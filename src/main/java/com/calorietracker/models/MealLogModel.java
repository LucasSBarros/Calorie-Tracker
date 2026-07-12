package com.calorietracker.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Armazena uma refeição efetivamente consumida por um usuário.
 *
 * O total calórico é preservado no momento do registro para manter o histórico
 * consistente quando os dados dos ingredientes forem alterados.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "meal_logs")
public class MealLogModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @UuidGenerator
    private UUID idMealLog;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "diet_id")
    private DietModel diet;

    @Column(nullable = false)
    private LocalDateTime consumedAt;

    private String description;

    @Column(nullable = false)
    private BigDecimal totalCalories = BigDecimal.ZERO;

    @OneToMany(mappedBy = "mealLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MealLogItemModel> items = new LinkedHashSet<>();
}
