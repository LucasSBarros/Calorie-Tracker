package com.calorietracker.projections;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.calorietracker.models.Gender;

public interface UserReportProjection {

    UUID getIdUser();

    String getName();

    String getEmail();

    BigDecimal getWeight();

    BigDecimal getHeight();

    LocalDate getBirthDate();

    Gender getGender();

    BigDecimal getImc();

    BigDecimal getTmb();
}