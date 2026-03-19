package com.calorietracker.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.calorietracker.dtos.AuthResponseDto;
import com.calorietracker.dtos.LoginRequestDto;
import com.calorietracker.dtos.RegisterRequestDto;
import com.calorietracker.services.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST - /api/auth/register, Rota de cadastro de usuário.
     * 
     * @param request
     * @return token de autenticação
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody @Valid RegisterRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    /**
     * POST - /api/auth/login, Rota de autenticação do usuário.
     * 
     * @param request
     * @return token de autenticação
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }
}