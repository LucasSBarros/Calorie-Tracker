package com.calorietracker.services;

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
     * para PDF utilizando OpenHTMLToPDF.
     *
     * @param userId identificador do usuário
     * @return arquivo PDF em formato de array de bytes
     */
    byte[] generateUserReportPdf(UUID userId);
}