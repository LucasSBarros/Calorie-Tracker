package com.calorietracker.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.calorietracker.dtos.response.GoalResponse;
import com.calorietracker.dtos.request.GoalRequest;
import com.calorietracker.exceptions.ConflictException;
import com.calorietracker.exceptions.ResourceNotFoundException;
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
    private final GoalMapper goalMapper;

    @Override
    public GoalResponse create(GoalRequest request) {
        if (goalRepository.existsByUser_IdUser(request.userId())) {
            throw new ConflictException("User already has a goal: " + request.userId());
        }

        var user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.userId()));

        var goal = goalMapper.toEntity(request);
        goal.setUser(user);

        var saved = goalRepository.save(goal);
        return goalMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoalResponse> findAll() {
        return goalRepository.findAll()
                .stream()
                .map(goalMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GoalResponse> findById(UUID id) {
        return goalRepository.findById(id).map(goalMapper::toResponse);
    }

    @Override
    public Optional<GoalResponse> update(UUID id, GoalRequest request) {

        Optional<GoalResponse> result = Optional.empty();

        Optional<GoalModel> goalOpt = goalRepository.findById(id);

        if (goalOpt.isPresent()) {

            var existing = goalOpt.get();

            var user = userRepository.findById(request.userId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.userId()));

            if (!existing.getUser().getIdUser().equals(request.userId())
                    && goalRepository.existsByUser_IdUser(request.userId())) {
                throw new ConflictException("User already has a goal: " + request.userId());
            }

            goalMapper.updateEntityFromRequest(request, existing);
            existing.setUser(user);

            var saved = goalRepository.save(existing);

            result = Optional.of(goalMapper.toResponse(saved));
        }

        return result;
    }

    @Override
    public void delete(UUID id) {
        if (!goalRepository.existsById(id)) {
            throw new ResourceNotFoundException("Goal not found: " + id);
        }

        goalRepository.deleteById(id);
    }
}
