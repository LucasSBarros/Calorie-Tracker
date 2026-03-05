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
import com.calorietracker.repositories.GoalRepository;
import com.calorietracker.services.GoalService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;
    private final GoalMapper goalMapper;

    @Override
    public GoalDto create(GoalRequestDto request) {
        GoalModel goal = goalMapper.toEntity(request);
        return goalMapper.toDto(goalRepository.save(goal));
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
        return goalRepository.findById(id).map(existing -> {
            goalMapper.updateEntityFromDto(request, existing);
            GoalModel saved = goalRepository.save(existing);
            return goalMapper.toDto(saved);
        });
    }

    @Override
    public boolean delete(UUID id) {
        goalRepository.deleteById(id);
        return true;
    }
}