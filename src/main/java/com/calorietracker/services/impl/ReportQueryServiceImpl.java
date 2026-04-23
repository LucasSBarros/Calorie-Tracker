package com.calorietracker.services.impl;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.calorietracker.dtos.DietReportDto;
import com.calorietracker.dtos.MealIngredientReportDto;
import com.calorietracker.dtos.MealReportDto;
import com.calorietracker.dtos.UserProgressDto;
import com.calorietracker.dtos.UserReportDataDto;
import com.calorietracker.exceptions.ResourceNotFoundException;
import com.calorietracker.models.DietModel;
import com.calorietracker.models.GoalModel;
import com.calorietracker.models.MealIngredientModel;
import com.calorietracker.models.MealModel;
import com.calorietracker.models.StatusModel;
import com.calorietracker.projections.UserReportProjection;
import com.calorietracker.repositories.DietRepository;
import com.calorietracker.repositories.GoalRepository;
import com.calorietracker.repositories.StatusRepository;
import com.calorietracker.repositories.UserRepository;
import com.calorietracker.services.ReportQueryService;
import com.calorietracker.services.StatusService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportQueryServiceImpl implements ReportQueryService {

        private final UserRepository userRepository;
        private final GoalRepository goalRepository;
        private final StatusRepository statusRepository;
        private final DietRepository dietRepository;
        private final StatusService statusService;

        @Override
        @Transactional(readOnly = true)
        public UserReportDataDto getUserReportData(UUID userId) {

                UserReportProjection user = userRepository.findReportDataById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

                GoalModel goal = goalRepository.findByUser_IdUser(userId).orElse(null);

                StatusModel latestStatus = statusRepository
                                .findFirstByUser_IdUserOrderByCreatedAtDesc(userId)
                                .orElse(null);

                UserProgressDto progress = statusService.getUserProgress(userId).orElse(null);

                List<DietReportDto> diets = buildDietReports(userId);

                return new UserReportDataDto(
                                user.getIdUser(),
                                user.getName(),
                                user.getEmail(),
                                user.getWeight(),
                                user.getHeight(),
                                user.getBirthDate(),
                                calculateAge(user.getBirthDate()),
                                user.getGender(),
                                user.getImc(),
                                user.getTmb(),

                                goal != null ? goal.getWeight() : null,
                                goal != null ? goal.getBf() : null,

                                latestStatus != null ? latestStatus.getWeight() : null,
                                latestStatus != null ? latestStatus.getBf() : null,

                                progress != null ? progress.weightProgressPercent() : null,
                                progress != null ? progress.bfProgressPercent() : null,

                                diets);
        }

        /**
         * Busca todas as dietas do usuário e converte os dados para o formato
         * utilizado no relatório em PDF.
         *
         * As dietas são retornadas ordenadas pela data inicial mais recente.
         *
         * @param userId identificador do usuário
         * 
         * @return lista de dietas formatadas para o relatório
         */
        private List<DietReportDto> buildDietReports(UUID userId) {
                return dietRepository.findByUser_IdUserOrderByInitialDateDesc(userId)
                                .stream()
                                .map(this::mapDietToReportDto)
                                .toList();
        }

        /**
         * Converte uma dieta da entidade para o DTO utilizado no relatório.
         *
         * Também realiza o mapeamento das refeições associadas à dieta.
         *
         * @param diet dieta a ser convertida
         * @return DTO da dieta para o relatório
         */
        private DietReportDto mapDietToReportDto(DietModel diet) {
                List<MealReportDto> meals = mapMeals(diet);

                return new DietReportDto(
                                diet.getIdDiet(),
                                diet.getName(),
                                diet.getTotalCalories(),
                                diet.getInitialDate(),
                                diet.getFinalDate(),
                                meals);
        }

        /**
         * Converte as refeições de uma dieta para o formato utilizado no relatório.
         *
         * Caso a dieta não possua refeições, retorna uma lista vazia.
         *
         * @param diet dieta que contém as refeições
         * @return lista de refeições formatadas para o relatório
         */
        private List<MealReportDto> mapMeals(DietModel diet) {
                if (diet.getMeals() == null) {
                        return List.of();
                }

                return diet.getMeals()
                                .stream()
                                .map(this::mapMealToReportDto)
                                .toList();
        }

        /**
         * Converte uma refeição da entidade para o DTO utilizado no relatório.
         *
         * Também realiza o mapeamento dos ingredientes associados à refeição.
         *
         * @param meal refeição a ser convertida
         * 
         * @return DTO da refeição para o relatório
         */
        private MealReportDto mapMealToReportDto(MealModel meal) {
                List<MealIngredientReportDto> ingredients = mapMealIngredients(meal);

                return new MealReportDto(
                                meal.getIdMeal(),
                                meal.getMealDateTime(),
                                meal.getDescription(),
                                meal.getTotalCaloriesPerMeal(),
                                ingredients);
        }

        /**
         * Converte os ingredientes de uma refeição para o formato utilizado no
         * relatório.
         *
         * Caso a refeição não possua ingredientes, retorna uma lista vazia.
         *
         * @param meal refeição que contém os ingredientes
         * @return lista de ingredientes formatados para o relatório
         */
        private List<MealIngredientReportDto> mapMealIngredients(MealModel meal) {
                if (meal.getMealIngredients() == null) {
                        return List.of();
                }

                return meal.getMealIngredients()
                                .stream()
                                .map(this::mapMealIngredientToReportDto)
                                .toList();
        }

        /**
         * Converte um item de ingrediente da refeição para o DTO utilizado no
         * relatório.
         * 
         * Também calcula as calorias correspondentes ao peso informado.
         * 
         * @param item item da refeição a ser convertido
         * 
         * @return DTO do ingrediente para o relatório
         */
        private MealIngredientReportDto mapMealIngredientToReportDto(MealIngredientModel item) {

                String ingredientName = null;

                if (item.getIngredient() != null) {
                        ingredientName = item.getIngredient().getName();
                }

                return new MealIngredientReportDto(
                                item.getIdMealIngredient(),
                                item.getWeight(),
                                ingredientName,
                                item.calculateCalories());
        }

        /**
         * Calcula a idade do usuário com base na data de nascimento.
         * 
         * @param birthDate data de nascimento do usuário
         * @return idade atual em anos
         */
        private int calculateAge(LocalDate birthDate) {
                return Period.between(birthDate, LocalDate.now()).getYears();
        }
}