package com.calorietracker.services;

import java.time.LocalDate;

import com.calorietracker.dtos.report.ProgressReportResponse;

/**
 * Define a geração do relatório consolidado de progresso calórico.
 */
public interface ProgressReportService {

    /**
     * Gera o relatório do usuário para o período solicitado.
     *
     * Datas ausentes são preenchidas pelo serviço com o período padrão.
     *
     * @param userEmail e-mail do usuário autenticado
     * @param from primeira data incluída ou nula
     * @param to última data incluída ou nula
     * @return relatório consolidado de progresso
     */
    ProgressReportResponse generate(String userEmail, LocalDate from, LocalDate to);
}
