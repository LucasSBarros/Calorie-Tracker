package com.calorietracker.services;

import com.calorietracker.dtos.AuthResponseDto;
import com.calorietracker.dtos.LoginRequestDto;
import com.calorietracker.dtos.RegisterRequestDto;

public interface AuthService {

    /**
     * Método responsável por registrar um novo usuário e gerar o token de
     * autenticação.
     * 
     * @param request
     * @return token de autenticação
     */
    AuthResponseDto register(RegisterRequestDto request);

    /**
     * Método responsável por autenticar um usuário e gerar o token JWT.
     * 
     * @param request
     * @return token de autentação
     */
    AuthResponseDto login(LoginRequestDto request);
}