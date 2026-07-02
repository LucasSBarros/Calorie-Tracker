package com.calorietracker.controllers;

import java.util.UUID;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.calorietracker.services.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

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
                        .filename("relatorio-nutricional-" + userId + ".pdf")
                        .build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }
}
