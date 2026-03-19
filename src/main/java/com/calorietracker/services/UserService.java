package com.calorietracker.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.calorietracker.dtos.UserDto;
import com.calorietracker.dtos.UserRequestDto;

public interface UserService {

    /**
     * Método de criação de um usuário
     * 
     * @param request
     * @return
     */
    UserDto create(UserRequestDto request);

    /**
     * Método de listagem dos usuários
     * 
     * @return
     */
    List<UserDto> findAll();

    /**
     * Método de busca de usuário por Id
     * 
     * @param id
     * @return
     */
    Optional<UserDto> findById(UUID id);

    /**
     * Método de atualização de um usuário
     * 
     * @param id
     * @param request
     * @return
     */
    Optional<UserDto> update(UUID id, UserRequestDto request);

    /**
     * Método de deletar um usuário
     * 
     * @param id
     * @return
     */
    void delete(UUID id);
}