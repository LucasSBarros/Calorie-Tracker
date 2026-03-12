package com.calorietracker.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.calorietracker.dtos.IngredientDto;
import com.calorietracker.dtos.IngredientRequestDto;
import com.calorietracker.mappers.IngredientMapper;
import com.calorietracker.models.IngredientModel;
import com.calorietracker.repositories.IngredientRepository;
import com.calorietracker.services.IngredientService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;

    @Override
    public IngredientDto create(IngredientRequestDto request) {
        IngredientModel ingredient = ingredientMapper.toEntity(request);
        return ingredientMapper.toDto(ingredientRepository.save(ingredient));
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngredientDto> findAll() {
        return ingredientRepository.findAll()
                .stream()
                .map(ingredientMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IngredientDto> findById(UUID id) {
        return ingredientRepository.findById(id).map(ingredientMapper::toDto);
    }

    @Override
    public Optional<IngredientDto> update(UUID id, IngredientRequestDto request) {

        Optional<IngredientDto> result = Optional.empty();

        Optional<IngredientModel> ingredientOpt = ingredientRepository.findById(id);

        if (ingredientOpt.isPresent()) {

            IngredientModel existing = ingredientOpt.get();

            ingredientMapper.updateEntityFromDto(request, existing);

            IngredientModel saved = ingredientRepository.save(existing);

            result = Optional.of(ingredientMapper.toDto(saved));
        }

        return result;
    }

    @Override
    public boolean delete(UUID id) {
        ingredientRepository.deleteById(id);
        return true;
    }
}