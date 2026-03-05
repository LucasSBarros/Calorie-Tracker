package com.calorietracker.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.calorietracker.dtos.StatusDto;
import com.calorietracker.dtos.StatusRequestDto;
import com.calorietracker.mappers.StatusMapper;
import com.calorietracker.models.StatusModel;
import com.calorietracker.repositories.StatusRepository;
import com.calorietracker.services.StatusService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;
    private final StatusMapper statusMapper;

    @Override
    public StatusDto create(StatusRequestDto request) {
        StatusModel status = statusMapper.toEntity(request);
        return statusMapper.toDto(statusRepository.save(status));
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
        return statusRepository.findById(id).map(existing -> {
            statusMapper.updateEntityFromDto(request, existing);
            StatusModel saved = statusRepository.save(existing);
            return statusMapper.toDto(saved);
        });
    }

    @Override
    public boolean delete(UUID id) {
        statusRepository.deleteById(id);
        return true;
    }
}