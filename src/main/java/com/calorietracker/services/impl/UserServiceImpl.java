package com.calorietracker.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.calorietracker.dtos.response.UserResponse;
import com.calorietracker.dtos.request.UserRequest;
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
    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponse> findById(UUID id) {
        return userRepository.findById(id).map(userMapper::toResponse);
    }

    @Override
    public Optional<UserResponse> update(UUID id, UserRequest request) {

        Optional<UserResponse> result = Optional.empty();

        Optional<UserModel> userOpt = userRepository.findById(id);

        if (userOpt.isPresent()) {

            var existing = userOpt.get();

            userMapper.updateEntityFromRequest(request, existing);

            var saved = userRepository.save(existing);

            result = Optional.of(userMapper.toResponse(saved));
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
