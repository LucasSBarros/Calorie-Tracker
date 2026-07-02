package com.calorietracker.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.calorietracker.dtos.response.UserResponse;
import com.calorietracker.dtos.request.UserRequest;

public interface UserService {

    /**
     * Método de listagem dos usuários
     * 
     * @return
     */
    List<UserResponse> findAll();

    /**
     * Método de busca de usuário por Id
     * 
     * @param id
     * @return
     */
    Optional<UserResponse> findById(UUID id);

    /**
     * Método de atualização de um usuário
     * 
     * @param id
     * @param request
     * @return
     */
    Optional<UserResponse> update(UUID id, UserRequest request);

    /**
     * Método de deletar um usuário
     * 
     * @param id
     * @return
     */
    void delete(UUID id);
}