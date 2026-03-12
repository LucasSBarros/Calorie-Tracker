package com.calorietracker.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.calorietracker.dtos.StatusDto;
import com.calorietracker.dtos.StatusRequestDto;
import com.calorietracker.dtos.UserProgressDto;
import com.calorietracker.mappers.StatusMapper;
import com.calorietracker.models.GoalModel;
import com.calorietracker.models.StatusModel;
import com.calorietracker.models.UserModel;
import com.calorietracker.repositories.GoalRepository;
import com.calorietracker.repositories.StatusRepository;
import com.calorietracker.repositories.UserRepository;
import com.calorietracker.services.StatusService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    private final StatusRepository statusRepository;
    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final StatusMapper statusMapper;

    @Override
    public StatusDto create(StatusRequestDto request) {
        UserModel user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + request.userId()));

        StatusModel status = statusMapper.toEntity(request);
        status.setUser(user);

        StatusModel saved = statusRepository.save(status);
        return statusMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatusDto> findAll() {
        return statusRepository.findAll()
                .stream()
                .map(statusMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StatusDto> findById(UUID id) {
        return statusRepository.findById(id).map(statusMapper::toDto);
    }

    @Override
    public Optional<StatusDto> update(UUID id, StatusRequestDto request) {

        Optional<StatusDto> result = Optional.empty();

        Optional<StatusModel> statusOpt = statusRepository.findById(id);

        if (statusOpt.isPresent()) {

            StatusModel existing = statusOpt.get();

            UserModel user = userRepository.findById(request.userId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + request.userId()));

            statusMapper.updateEntityFromDto(request, existing);
            existing.setUser(user);

            StatusModel saved = statusRepository.save(existing);

            result = Optional.of(statusMapper.toDto(saved));
        }

        return result;
    }

    @Override
    public boolean delete(UUID id) {
        statusRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserProgressDto> getUserProgress(UUID userId) {

        Optional<UserProgressDto> result = Optional.empty();

        Optional<GoalModel> goalOpt = goalRepository.findByUser_IdUser(userId);
        Optional<StatusModel> currentStatusOpt = statusRepository.findFirstByUser_IdUserOrderByCreatedAtDesc(userId);

        if (goalOpt.isPresent() && currentStatusOpt.isPresent()) {

            GoalModel goal = goalOpt.get();
            StatusModel currentStatus = currentStatusOpt.get();

            BigDecimal weightProgressPercent = calculateProgressPercent(
                    goal.getStartWeight(),
                    currentStatus.getWeight(),
                    goal.getWeight());

            BigDecimal bfProgressPercent = calculateProgressPercent(
                    goal.getStartBf(),
                    currentStatus.getBf(),
                    goal.getBf());

            result = Optional.of(new UserProgressDto(
                    userId,
                    currentStatus.getWeight(),
                    goal.getWeight(),
                    weightProgressPercent,
                    currentStatus.getBf(),
                    goal.getBf(),
                    bfProgressPercent));
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatusDto> findHistoryByUser(UUID userId) {
        return statusRepository.findByUser_IdUserOrderByCreatedAtDesc(userId)
                .stream()
                .map(statusMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatusDto> findByUserAndPeriod(UUID userId, LocalDateTime start, LocalDateTime end) {
        return statusRepository.findByUser_IdUserAndCreatedAtBetweenOrderByCreatedAtDesc(userId, start, end)
                .stream()
                .map(statusMapper::toDto)
                .toList();
    }

    /**
     * Método para cálculo do progresso em porcentual, funcional tanto para o
     * porcentual de gordura corporal quando para o peso.
     * 
     * @param start
     * @param current
     * @param target
     * @return
     */
    private BigDecimal calculateProgressPercent(BigDecimal start, BigDecimal current, BigDecimal target) {
        BigDecimal result = null;

        if (start != null && current != null && target != null) {
            BigDecimal totalDistance = target.subtract(start).abs();

            if (totalDistance.compareTo(BigDecimal.ZERO) == 0) {
                result = ONE_HUNDRED;
            } else {
                BigDecimal coveredDistance = current.subtract(start).abs();

                BigDecimal progress = coveredDistance
                        .multiply(ONE_HUNDRED)
                        .divide(totalDistance, 2, RoundingMode.HALF_UP);

                if (progress.compareTo(ONE_HUNDRED) > 0) {
                    result = ONE_HUNDRED;
                } else {
                    result = progress.max(BigDecimal.ZERO);
                }
            }
        }

        return result;
    }
}