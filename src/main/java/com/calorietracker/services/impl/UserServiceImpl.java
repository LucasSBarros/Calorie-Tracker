package com.calorietracker.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.calorietracker.dtos.UserDto;
import com.calorietracker.dtos.UserRequestDto;
import com.calorietracker.exceptions.ResourceNotFoundException;
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

        Optional<UserDto> result = Optional.empty();

        Optional<UserModel> userOpt = userRepository.findById(id);

        if (userOpt.isPresent()) {

            UserModel existing = userOpt.get();

            userMapper.updateEntityFromDto(request, existing);

            UserModel saved = userRepository.save(existing);

            result = Optional.of(userMapper.toDto(saved));
        }

        return result;
    }

    @Override
    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found: " + id);
        }

        userRepository.deleteById(id);
    }
}