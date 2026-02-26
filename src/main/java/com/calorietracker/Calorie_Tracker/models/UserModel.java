package com.calorietracker.Calorie_Tracker.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
@Table(name = "users")
public class UserModel implements Serializable {

    @Id
    @UuidGenerator
    private UUID idUser; // ID

    private String name; // Nome

    private BigDecimal weight; // Peso

    private BigDecimal height; // Altura

    private Long age; // Idade

    @Enumerated(EnumType.STRING)
    private Gender gender; // Gênero

    private BigDecimal imc; // Índice de Massa Corporal

    private BigDecimal tmb; // Taxa Metabólica Basal

    /**
     * Método que realiza o cálculo do IMC com base na altura e no peso do usuário.
     * 
     * @return
     */
    public BigDecimal calculateImc() {
        BigDecimal result = null;

        if (weight != null
                && height != null
                && height.compareTo(BigDecimal.ZERO) > 0) {

            BigDecimal h2 = height.multiply(height);
            result = weight.divide(h2, 2, RoundingMode.HALF_UP);
        }

        return result;
    }

    /**
     * Método que realiza o cálculo do TMB com base na altura, no peso, na idade e
     * no gênero do usuário, através da fórmula de Mifflin-St Jeor.
     * 
     * @return
     */
    public BigDecimal calculateTmb() {

        BigDecimal result = null;

        if (weight != null
                && height != null
                && age != null
                && gender != null) {

            BigDecimal heightCm = height.multiply(BigDecimal.valueOf(100));

            BigDecimal base = BigDecimal.valueOf(10).multiply(weight)
                    .add(BigDecimal.valueOf(6.25).multiply(heightCm))
                    .subtract(BigDecimal.valueOf(5).multiply(BigDecimal.valueOf(age)));

            BigDecimal genderAdjust = (gender == Gender.MALE)
                    ? BigDecimal.valueOf(5)
                    : BigDecimal.valueOf(-161);

            result = base.add(genderAdjust)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return result;
    }

    @PrePersist
    @PreUpdate
    private void updateDerivedFields() {
        this.imc = calculateImc();
        this.tmb = calculateTmb();
    }

}
