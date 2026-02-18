package com.calorietracker.Calorie_Tracker.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.calorietracker.Calorie_Tracker.dtos.DietDto;
import com.calorietracker.Calorie_Tracker.dtos.DietRequestDto;

public interface DietService {

    /**
     * Método de criação de uma dieta
     * 
     * @param request
     * @return
     */
    DietDto create(DietRequestDto request);

    /**
     * Método de listagem das dietas
     * 
     * @return
     */
    List<DietDto> findAll();

    /**
     * Método de busca de dieta por Id
     * 
     * @param id
     * @return
     */
    Optional<DietDto> findById(UUID id);

    /**
     * Método de atualização de uma dieta
     * 
     * @param id
     * @param request
     * @return
     */
    Optional<DietDto> update(UUID id, DietRequestDto request);

    /**
     * Método de deletar uma dieta
     * 
     * @param id
     * @return
     */
    boolean delete(UUID id);
}