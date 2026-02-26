package com.calorietracker.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.calorietracker.dtos.MealDto;
import com.calorietracker.dtos.MealRequestDto;

public interface MealService {

    /**
     * Método de criação de uma refeição
     * 
     * @param request
     * @return
     */
    MealDto create(MealRequestDto request);

    /**
     * Método de listagem das refeições
     * 
     * @return
     */
    List<MealDto> findAll();

    /**
     * Método de busca de refeição por Id
     * 
     * @param id
     * @return
     */
    Optional<MealDto> findById(UUID id);

    /**
     * Método de atualização de uma refeição
     * 
     * @param id
     * @param request
     * @return
     */
    Optional<MealDto> update(UUID id, MealRequestDto request);

    /**
     * Método de deletar uma refeição
     * 
     * @param id
     * @return
     */
    boolean delete(UUID id);
}