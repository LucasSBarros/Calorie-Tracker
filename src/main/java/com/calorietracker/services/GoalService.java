package com.calorietracker.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.calorietracker.dtos.response.GoalResponse;
import com.calorietracker.dtos.request.GoalRequest;

public interface GoalService {

    /**
     * Método de criação de um objetivo
     * 
     * @param request
     * @return
     */
    GoalResponse create(GoalRequest request);

    /**
     * Método de listagem de objetivos
     * 
     * @return
     */
    List<GoalResponse> findAll();

    /**
     * Método de busca de objetivo por Id
     * 
     * @param id
     * @return
     */
    Optional<GoalResponse> findById(UUID id);

    /**
     * Método de atualização de um objetivo
     * 
     * @param id
     * @param request
     * @return
     */
    Optional<GoalResponse> update(UUID id, GoalRequest request);

    /**
     * Método de deletar um objetivo
     * 
     * @param id
     * @return
     */
    void delete(UUID id);
}