package com.calorietracker.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.calorietracker.dtos.response.IngredientResponse;
import com.calorietracker.dtos.request.IngredientRequest;
import com.calorietracker.exceptions.ResourceNotFoundException;
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
    public IngredientResponse create(IngredientRequest request) {
        var ingredient = ingredientMapper.toEntity(request);
        return ingredientMapper.toResponse(ingredientRepository.save(ingredient));
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngredientResponse> findAll() {
        return ingredientRepository.findAll()
                .stream()
                .map(ingredientMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IngredientResponse> findById(UUID id) {
        return ingredientRepository.findById(id).map(ingredientMapper::toResponse);
    }

    @Override
    public Optional<IngredientResponse> update(UUID id, IngredientRequest request) {

        Optional<IngredientResponse> result = Optional.empty();

        Optional<IngredientModel> ingredientOpt = ingredientRepository.findById(id);

        if (ingredientOpt.isPresent()) {

            var existing = ingredientOpt.get();

            ingredientMapper.updateEntityFromRequest(request, existing);

            var saved = ingredientRepository.save(existing);

            result = Optional.of(ingredientMapper.toResponse(saved));
        }

        return result;
    }

    @Override
    public void delete(UUID id) {
        if (!ingredientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ingredient not found: " + id);
        }
        ingredientRepository.deleteById(id);
    }
}
