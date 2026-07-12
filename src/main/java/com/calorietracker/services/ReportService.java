package com.calorietracker.services;

import java.time.LocalDate;
import java.util.UUID;

public interface ReportService {

    /**
     * Gera o relatório completo do usuário em formato PDF.
     *
     * O relatório contém:
     * - dados cadastrais
     * - informações físicas e metabólicas
     * - meta atual
     * - último status registrado
     * - progresso de peso e percentual de gordura
     * - dietas cadastradas com refeições e ingredientes
     *
     * O conteúdo é montado a partir de um template XHTML e convertido
     * para PDF utilizando Flying Saucer.
     *
     * @param userId identificador do usuário
     * @return arquivo PDF em formato de array de bytes
     */
    byte[] generateUserReportPdf(UUID userId);

    /**
     * Gera o relatório de progresso calórico em formato PDF.
     *
     * O conteúdo apresenta dieta vigente, consumo diário, médias semanais,
     * comparação com a meta, tendência e dias sem refeições registradas.
     *
     * @param userEmail e-mail do usuário autenticado
     * @param from primeira data incluída ou nula
     * @param to última data incluída ou nula
     * @return arquivo PDF em formato de array de bytes
     */
    byte[] generateProgressReportPdf(String userEmail, LocalDate from, LocalDate to);
}
