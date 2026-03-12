package com.calorietracker.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserModel user; // Usuário

    private BigDecimal weight; // Meta de Peso 

     private BigDecimal startWeight; // Peso Inicial

    private BigDecimal bf; // Meta de Gordura corporal

     private BigDecimal startBf; // Peso Final

}
