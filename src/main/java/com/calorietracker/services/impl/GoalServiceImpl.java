package com.calorietracker.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.calorietracker.dtos.GoalDto;
import com.calorietracker.dtos.GoalRequestDto;
import com.calorietracker.mappers.GoalMapper;
import com.calorietracker.models.GoalModel;
import com.calorietracker.models.UserModel;
import com.calorietracker.repositories.GoalRepository;
import com.calorietracker.repositories.StatusRepository;
import com.calorietracker.repositories.UserRepository;
import com.calorietracker.services.GoalService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;
    private final GoalMapper goalMapper;

    @Override
    public GoalDto create(GoalRequestDto request) {
        if (goalRepository.existsByUser_IdUser(request.userId())) {
            throw new IllegalArgumentException("User already has a goal: " + request.userId());
        }

        UserModel user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + request.userId()));

        GoalModel goal = goalMapper.toEntity(request);
        goal.setUser(user);

        goal.setStartWeight(user.getWeight());

        statusRepository.findFirstByUser_IdUserOrderByCreatedAtDesc(request.userId())
                .ifPresent(status -> goal.setStartBf(status.getBf()));

        GoalModel saved = goalRepository.save(goal);
        return goalMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoalDto> findAll() {
        return goalRepository.findAll()
                .stream()
                .map(goalMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GoalDto> findById(UUID id) {
        return goalRepository.findById(id).map(goalMapper::toDto);
    }

    @Override
    public Optional<GoalDto> update(UUID id, GoalRequestDto request) {

        Optional<GoalDto> result = Optional.empty();

        Optional<GoalModel> goalOpt = goalRepository.findById(id);

        if (goalOpt.isPresent()) {

            GoalModel existing = goalOpt.get();

            UserModel user = userRepository.findById(request.userId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + request.userId()));

            goalMapper.updateEntityFromDto(request, existing);
            existing.setUser(user);

            GoalModel saved = goalRepository.save(existing);

            result = Optional.of(goalMapper.toDto(saved));
        }

        return result;
    }

    @Override
    public boolean delete(UUID id) {
        goalRepository.deleteById(id);
        return true;
    }
}