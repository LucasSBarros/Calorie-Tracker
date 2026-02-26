package com.calorietracker.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.calorietracker.dtos.GoalDto;
import com.calorietracker.dtos.GoalRequestDto;

public interface GoalService {

    /**
     * Método de criação de um objetivo
     * 
     * @param request
     * @return
     */
    GoalDto create(GoalRequestDto request);

    /**
     * Método de listagem de objetivos
     * 
     * @return
     */
    List<GoalDto> findAll();

    /**
     * Método de busca de objetivo por Id
     * 
     * @param id
     * @return
     */
    Optional<GoalDto> findById(UUID id);

    /**
     * Método de atualização de um objetivo
     * 
     * @param id
     * @param request
     * @return
     */
    Optional<GoalDto> update(UUID id, GoalRequestDto request);

    /**
     * Método de deletar um objetivo
     * 
     * @param id
     * @return
     */
    boolean delete(UUID id);
}