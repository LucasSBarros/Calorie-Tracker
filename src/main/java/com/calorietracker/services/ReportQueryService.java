package com.calorietracker.services;

import java.util.UUID;

import com.calorietracker.dtos.UserReportDataDto;

public interface ReportQueryService {

    /**
     * Busca e consolida todos os dados necessários para geração do relatório
     * completo do usuário.
     *
     * O relatório inclui:
     * - dados cadastrais do usuário
     * - informações físicas e metabólicas
     * - meta atual (goal)
     * - último status registrado
     * - progresso percentual de peso e percentual de gordura
     * - dietas cadastradas com refeições e ingredientes
     *
     * @param userId identificador do usuário
     * @return objeto contendo todos os dados necessários para geração do relatório
     */
    UserReportDataDto getUserReportData(UUID userId);

}