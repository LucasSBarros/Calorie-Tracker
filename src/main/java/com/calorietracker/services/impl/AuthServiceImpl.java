package com.calorietracker.services.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.calorietracker.dtos.AuthResponseDto;
import com.calorietracker.dtos.LoginRequestDto;
import com.calorietracker.dtos.RegisterRequestDto;
import com.calorietracker.exceptions.ConflictException;
import com.calorietracker.exceptions.UnauthorizedException;
import com.calorietracker.models.UserModel;
import com.calorietracker.repositories.UserRepository;
import com.calorietracker.security.JwtService;
import com.calorietracker.services.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponseDto register(RegisterRequestDto request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email already in use: " + request.email());
        }

        UserModel user = new UserModel();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setWeight(request.weight());
        user.setHeight(request.height());
        user.setBirthDate(request.birthDate());
        user.setGender(request.gender());

        UserModel saved = userRepository.save(user);

        String token = jwtService.generateToken(saved);

        return new AuthResponseDto(token);
    }

    @Override
    public AuthResponseDto login(LoginRequestDto request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()));
        } catch (BadCredentialsException ex) {
            throw new UnauthorizedException("Invalid credentials");
        }

        UserModel user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        String token = jwtService.generateToken(user);

        return new AuthResponseDto(token);
    }
}