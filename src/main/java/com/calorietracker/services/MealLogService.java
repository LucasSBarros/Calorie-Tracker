package com.calorietracker.services;

import java.time.LocalDate;
import java.util.List;

import com.calorietracker.dtos.request.MealLogRequest;
import com.calorietracker.dtos.response.MealLogResponse;

/**
 * Define as operações de registro e consulta de refeições consumidas.
 */
public interface MealLogService {

    /**
     * Registra uma refeição para o usuário identificado pelo e-mail autenticado.
     *
     * @param userEmail e-mail do usuário autenticado
     * @param request dados da refeição
     * @return refeição persistida
     */
    MealLogResponse create(String userEmail, MealLogRequest request);

    /**
     * Consulta as refeições do usuário dentro do intervalo informado.
     *
     * @param userEmail e-mail do usuário autenticado
     * @param from primeira data incluída
     * @param to última data incluída
     * @return refeições consumidas no período
     */
    List<MealLogResponse> findByPeriod(String userEmail, LocalDate from, LocalDate to);
}
