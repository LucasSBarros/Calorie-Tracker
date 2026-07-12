package com.calorietracker.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;

import com.calorietracker.dtos.request.MealLogRequest;
import com.calorietracker.dtos.response.MealLogItemResponse;
import com.calorietracker.dtos.response.MealLogResponse;
import com.calorietracker.exceptions.BusinessException;
import com.calorietracker.exceptions.ResourceNotFoundException;
import com.calorietracker.models.DietModel;
import com.calorietracker.models.IngredientModel;
import com.calorietracker.models.MealLogItemModel;
import com.calorietracker.models.MealLogModel;
import com.calorietracker.models.UserModel;
import com.calorietracker.repositories.DietRepository;
import com.calorietracker.repositories.IngredientRepository;
import com.calorietracker.repositories.MealLogRepository;
import com.calorietracker.repositories.UserRepository;
import com.calorietracker.services.MealLogService;

import lombok.RequiredArgsConstructor;

/**
 * Implementa o registro de consumo real e preserva os valores calóricos
 * calculados no momento da inclusão.
 */
@Service
@RequiredArgsConstructor
public class MealLogServiceImpl implements MealLogService {

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    private final MealLogRepository mealLogRepository;
    private final UserRepository userRepository;
    private final DietRepository dietRepository;
    private final IngredientRepository ingredientRepository;

    /**
     * Registra uma refeição consumida pelo usuário autenticado.
     *
     * Cada item tem suas calorias calculadas pela quantidade consumida e pelo
     * valor calórico atual do ingrediente.
     *
     * @param userEmail e-mail do usuário autenticado
     * @param request dados da refeição
     * @return refeição persistida
     */
    @Override
    @Transactional
    public MealLogResponse create(String userEmail, MealLogRequest request) {
        if (request.consumedAt().isAfter(LocalDateTime.now())) {
            throw new BusinessException("Meal consumption date cannot be in the future");
        }

        UserModel user = findUser(userEmail);
        MealLogModel mealLog = new MealLogModel();
        mealLog.setUser(user);
        mealLog.setDiet(resolveDiet(request.dietId(), user));
        mealLog.setConsumedAt(request.consumedAt());
        mealLog.setDescription(request.description());

        BigDecimal total = BigDecimal.ZERO;
        for (var itemRequest : request.items()) {
            IngredientModel ingredient = ingredientRepository.findById(itemRequest.ingredientId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Ingredient not found: " + itemRequest.ingredientId()));

            if (ingredient.getMacro() == null || ingredient.getMacro().getCalories() == null) {
                throw new BusinessException(
                        "Ingredient has no calorie information: " + ingredient.getIdIngredient());
            }

            BigDecimal calories = ingredient.getMacro().getCalories()
                    .multiply(itemRequest.weight())
                    .divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);

            MealLogItemModel item = new MealLogItemModel();
            item.setMealLog(mealLog);
            item.setIngredient(ingredient);
            item.setWeight(itemRequest.weight());
            item.setCalories(calories);
            mealLog.getItems().add(item);
            total = total.add(calories);
        }

        mealLog.setTotalCalories(total.setScale(2, RoundingMode.HALF_UP));
        return toResponse(mealLogRepository.save(mealLog));
    }

    /**
     * Consulta refeições consumidas pelo usuário no período informado.
     *
     * @param userEmail e-mail do usuário autenticado
     * @param from primeira data incluída
     * @param to última data incluída
     * @return refeições encontradas no período
     */
    @Override
    @Transactional(readOnly = true)
    public List<MealLogResponse> findByPeriod(String userEmail, LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new BusinessException("'from' must be before or equal to 'to'");
        }

        UserModel user = findUser(userEmail);
        return mealLogRepository
                .findByUser_IdUserAndConsumedAtGreaterThanEqualAndConsumedAtLessThanOrderByConsumedAtDesc(
                        user.getIdUser(),
                        from.atStartOfDay(),
                        to.plusDays(1).atStartOfDay())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Localiza o usuário correspondente à identidade autenticada.
     *
     * @param email e-mail presente na autenticação
     * @return usuário encontrado
     */
    private UserModel findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }

    /**
     * Localiza a dieta opcional e valida se ela pertence ao usuário autenticado.
     *
     * @param dietId identificador da dieta ou nulo
     * @param user usuário autenticado
     * @return dieta encontrada ou nula
     */
    private DietModel resolveDiet(java.util.UUID dietId, UserModel user) {
        if (dietId == null) {
            return null;
        }

        DietModel diet = dietRepository.findById(dietId)
                .orElseThrow(() -> new ResourceNotFoundException("Diet not found: " + dietId));

        if (!diet.getUser().getIdUser().equals(user.getIdUser())) {
            throw new AccessDeniedException("Diet does not belong to the authenticated user");
        }

        return diet;
    }

    /**
     * Converte a entidade de consumo para o contrato exposto pela API.
     *
     * @param model registro persistido
     * @return resposta com refeição e itens
     */
    private MealLogResponse toResponse(MealLogModel model) {
        List<MealLogItemResponse> items = model.getItems().stream()
                .map(item -> new MealLogItemResponse(
                        item.getIdMealLogItem(),
                        item.getIngredient().getIdIngredient(),
                        item.getIngredient().getName(),
                        item.getWeight(),
                        item.getCalories()))
                .toList();

        return new MealLogResponse(
                model.getIdMealLog(),
                model.getConsumedAt(),
                model.getDescription(),
                model.getDiet() == null ? null : model.getDiet().getIdDiet(),
                model.getTotalCalories(),
                items);
    }
}
