package com.calorietracker.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.calorietracker.services.ReportService;
import com.calorietracker.dtos.report.ProgressReportResponse;
import com.calorietracker.services.ProgressReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final ProgressReportService progressReportService;

    /**
     * Gera o relatório JSON de progresso do usuário autenticado.
     *
     * Quando as datas não são fornecidas, o serviço considera os últimos 28
     * dias.
     *
     * @param principal identidade do usuário autenticado
     * @param from primeira data incluída ou nula
     * @param to última data incluída ou nula
     * @return relatório consolidado de consumo e aderência
     */
    @GetMapping("/progress")
    public ResponseEntity<ProgressReportResponse> generateProgressReport(
            Principal principal,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(progressReportService.generate(principal.getName(), from, to));
    }

    /**
     * Gera o relatório de progresso do usuário autenticado em formato PDF.
     *
     * @param principal identidade do usuário autenticado
     * @param from primeira data incluída ou nula
     * @param to última data incluída ou nula
     * @return relatório de progresso em PDF
     */
    @GetMapping("/progress/pdf")
    public ResponseEntity<byte[]> generateProgressReportPdf(
            Principal principal,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        var pdf = reportService.generateProgressReportPdf(principal.getName(), from, to);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.inline()
                        .filename(buildPdfFilename("relatorio-progresso"))
                        .build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }

    /**
     * GET - /api/reports/users/{userId}/pdf, Rota responsável por gerar
     * o relatório nutricional em PDF do usuário.
     * 
     * @param userId
     * @return arquivo PDF do relatório
     */
    @GetMapping("/users/{userId}/pdf")
    public ResponseEntity<byte[]> generateUserReport(@PathVariable UUID userId) {
        var pdf = reportService.generateUserReportPdf(userId);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.inline()
                        .filename(buildPdfFilename("relatorio-nutricional"))
                        .build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }

    /**
     * Monta o nome do arquivo PDF com a data atual.
     *
     * @param reportName nome base do relatório
     * @return nome do arquivo no formato nome-AAAA-MM-DD.pdf
     */
    private String buildPdfFilename(String reportName) {
        return reportName + "-" + LocalDate.now() + ".pdf";
    }
}
