package com.calorietracker.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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

    @Column(nullable = false, unique = true)
    private String email; // E-mail

    @Column(nullable = false)
    private String password; // Senha

    private BigDecimal weight; // Peso (armazenado em gramas)

    private LocalDate birthDate; // Data de nascimento

    private BigDecimal height; // Altura (Armazenado em centímetros)

    @Enumerated(EnumType.STRING)
    private Gender gender; // Gênero

    private BigDecimal imc; // Índice de Massa Corporal

    private BigDecimal tmb; // Taxa Metabólica Basal

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private GoalModel goal; // Objetivo

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StatusModel> statuses = new LinkedHashSet<>(); // Status

    /**
     * Constantes (Magic Numbers)
     */

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    private static final BigDecimal ONE_THOUSAND = BigDecimal.valueOf(1000);

    private static final BigDecimal TMB_WEIGHT = BigDecimal.valueOf(10);
    private static final BigDecimal TMB_HEIGHT = BigDecimal.valueOf(6.25);
    private static final BigDecimal TMB_AGE = BigDecimal.valueOf(5);

    private static final BigDecimal MALE_ADJUST = BigDecimal.valueOf(5);
    private static final BigDecimal FEMALE_ADJUST = BigDecimal.valueOf(-161);

    /**
     * Método que realiza o cálculo da idade do usuário.
     * 
     * @return birthDate
     */

    public int calculateAge() {
        return birthDate == null
                ? 0
                : Period.between(birthDate, LocalDate.now()).getYears();
    }

    /**
     * Método que realiza o cálculo do IMC com base na altura e no peso do usuário.
     * 
     * @return IMC
     */
    public BigDecimal calculateImc() {

        BigDecimal result = null;

        if (weight != null && height != null && height.signum() > 0) {

            BigDecimal weightKg = weight.divide(ONE_THOUSAND, 6, RoundingMode.HALF_UP);
            BigDecimal heightMeters = height.divide(ONE_HUNDRED, 6, RoundingMode.HALF_UP);

            BigDecimal heightSquared = heightMeters.multiply(heightMeters);

            result = weightKg
                    .divide(heightSquared, 2, RoundingMode.HALF_UP);
        }

        return result;
    }

    /**
     * Método que realiza o cálculo do TMB com base na altura, no peso, na idade e
     * no gênero do usuário, através da fórmula de Mifflin-St Jeor.
     * 
     * @return TBM
     */
    public BigDecimal calculateTmb() {

        BigDecimal result = null;

        if (weight != null && height != null && birthDate != null && gender != null) {

            int age = calculateAge();

            BigDecimal weightKg = weight.divide(ONE_THOUSAND, 6, RoundingMode.HALF_UP);

            BigDecimal base = TMB_WEIGHT.multiply(weightKg)
                    .add(TMB_HEIGHT.multiply(height))
                    .subtract(TMB_AGE.multiply(BigDecimal.valueOf(age)));

            BigDecimal genderAdjust = (gender == Gender.MALE)
                    ? MALE_ADJUST
                    : FEMALE_ADJUST;

            result = base
                    .add(genderAdjust)
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
