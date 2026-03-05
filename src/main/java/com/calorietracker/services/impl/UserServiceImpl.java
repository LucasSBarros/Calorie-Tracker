package com.calorietracker.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.calorietracker.dtos.UserDto;
import com.calorietracker.dtos.UserRequestDto;
import com.calorietracker.mappers.UserMapper;
import com.calorietracker.models.UserModel;
import com.calorietracker.repositories.UserRepository;
import com.calorietracker.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto create(UserRequestDto request) {
        UserModel user = userMapper.toEntity(request);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findById(UUID id) {
        return userRepository.findById(id).map(userMapper::toDto);
    }

    @Override
    public Optional<UserDto> update(UUID id, UserRequestDto request) {
        return userRepository.findById(id).map(existing -> {
            userMapper.updateEntityFromDto(request, existing);
            UserModel saved = userRepository.save(existing);
            return userMapper.toDto(saved);
        });
    }

    @Override
    public boolean delete(UUID id) {
        userRepository.deleteById(id);
        return true;
    }
}