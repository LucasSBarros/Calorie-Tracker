package com.calorietracker.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.calorietracker.dtos.response.MealResponse;
import com.calorietracker.dtos.request.MealRequest;

public interface MealService {

    /**
     * Método de criação de uma refeição
     * 
     * @param request
     * @return
     */
    MealResponse create(MealRequest request);

    /**
     * Método de listagem das refeições
     * 
     * @return
     */
    List<MealResponse> findAll();

    /**
     * Método de busca de refeição por Id
     * 
     * @param id
     * @return
     */
    Optional<MealResponse> findById(UUID id);

    /**
     * Método de atualização de uma refeição
     * 
     * @param id
     * @param request
     * @return
     */
    Optional<MealResponse> update(UUID id, MealRequest request);

    /**
     * Método de deletar uma refeição
     * 
     * @param id
     * @return
     */
    void delete(UUID id);
}