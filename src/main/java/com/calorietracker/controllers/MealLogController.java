package com.calorietracker.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.calorietracker.dtos.request.MealLogRequest;
import com.calorietracker.dtos.response.MealLogResponse;
import com.calorietracker.services.MealLogService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Expõe operações para registrar e consultar refeições consumidas pelo usuário
 * autenticado.
 */
@RestController
@RequestMapping("/api/meal-logs")
@RequiredArgsConstructor
public class MealLogController {

    private final MealLogService mealLogService;

    /**
     * Registra uma refeição consumida para o usuário autenticado.
     *
     * @param principal identidade do usuário autenticado
     * @param request dados da refeição consumida
     * @return refeição persistida com calorias calculadas
     */
    @PostMapping
    public ResponseEntity<MealLogResponse> create(
            Principal principal,
            @RequestBody @Valid MealLogRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mealLogService.create(principal.getName(), request));
    }

    /**
     * Consulta as refeições consumidas pelo usuário dentro de um período.
     *
     * @param principal identidade do usuário autenticado
     * @param from primeira data incluída
     * @param to última data incluída
     * @return refeições encontradas em ordem decrescente de consumo
     */
    @GetMapping
    public ResponseEntity<List<MealLogResponse>> findByPeriod(
            Principal principal,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(mealLogService.findByPeriod(principal.getName(), from, to));
    }
}
