package com.calorietracker.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SequencedCollection;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.calorietracker.dtos.report.ProgressReportResponse;
import com.calorietracker.dtos.report.ProgressReportResponse.CurrentDiet;
import com.calorietracker.dtos.report.ProgressReportResponse.DailyCalories;
import com.calorietracker.dtos.report.ProgressReportResponse.GoalComparison;
import com.calorietracker.dtos.report.ProgressReportResponse.ProgressTrend;
import com.calorietracker.dtos.report.ProgressReportResponse.ReportPeriod;
import com.calorietracker.dtos.report.ProgressReportResponse.WeeklyAverage;
import com.calorietracker.exceptions.BusinessException;
import com.calorietracker.exceptions.ResourceNotFoundException;
import com.calorietracker.models.DietModel;
import com.calorietracker.models.UserModel;
import com.calorietracker.projections.MealLogCaloriesProjection;
import com.calorietracker.repositories.DietRepository;
import com.calorietracker.repositories.MealLogRepository;
import com.calorietracker.repositories.UserRepository;
import com.calorietracker.services.ProgressReportService;

import lombok.RequiredArgsConstructor;

/**
 * Consolida registros de consumo em indicadores diários, semanais e de
 * tendência em relação à meta da dieta vigente.
 */
@Service
@RequiredArgsConstructor
public class ProgressReportServiceImpl implements ProgressReportService {

    private static final int DEFAULT_PERIOD_DAYS = 28;
    private static final int MAX_PERIOD_DAYS = 366;
    private static final int TREND_WINDOW_DAYS = 7;
    private static final Map<String, BigDecimal> TREND_LIMITS = Map.of(
            "stableDistance", BigDecimal.valueOf(25));

    private final UserRepository userRepository;
    private final DietRepository dietRepository;
    private final MealLogRepository mealLogRepository;

    /**
     * Gera o relatório do usuário autenticado no período solicitado.
     *
     * Dias sem registros são incluídos com consumo zero para que médias e
     * tendências representem todo o intervalo.
     *
     * @param userEmail e-mail do usuário autenticado
     * @param requestedFrom primeira data solicitada ou nula
     * @param requestedTo última data solicitada ou nula
     * @return relatório consolidado
     */
    @Override
    @Transactional(readOnly = true)
    public ProgressReportResponse generate(String userEmail, LocalDate requestedFrom, LocalDate requestedTo) {
        LocalDate today = LocalDate.now();
        LocalDate to = requestedTo == null ? today : requestedTo;
        LocalDate from = requestedFrom == null ? to.minusDays(DEFAULT_PERIOD_DAYS - 1L) : requestedFrom;
        validatePeriod(from, to, today);

        UserModel user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        DietModel currentDiet = dietRepository.findCurrentByUserId(user.getIdUser(), today)
                .stream()
                .findFirst()
                .orElse(null);
        BigDecimal target = currentDiet == null ? null : currentDiet.getDailyCalorieTarget();

        List<MealLogCaloriesProjection> meals = mealLogRepository.findCaloriesByUserAndPeriod(
                user.getIdUser(),
                from.atStartOfDay(),
                to.plusDays(1).atStartOfDay());

        Map<LocalDate, BigDecimal> caloriesByDay = meals.isEmpty()
                ? Map.of()
                : meals.stream().collect(Collectors.toMap(
                        meal -> meal.consumedAt().toLocalDate(),
                        MealLogCaloriesProjection::calories,
                        BigDecimal::add,
                        LinkedHashMap::new));

        Map<LocalDate, Long> mealCountByDay = meals.stream().collect(Collectors.groupingBy(
                meal -> meal.consumedAt().toLocalDate(),
                LinkedHashMap::new,
                Collectors.counting()));

        List<DailyCalories> dailyCalories = from.datesUntil(to.plusDays(1))
                .map(date -> toDailyCalories(
                        date,
                        caloriesByDay.getOrDefault(date, BigDecimal.ZERO),
                        mealCountByDay.getOrDefault(date, 0L),
                        target))
                .toList();

        SequencedCollection<DailyCalories> orderedDays = new ArrayList<>(dailyCalories);
        List<WeeklyAverage> weeklyAverages = buildWeeklyAverages(orderedDays, target);
        GoalComparison comparison = buildGoalComparison(orderedDays, target);
        ProgressTrend trend = buildTrend(orderedDays, target, today);
        List<LocalDate> daysWithoutMeals = orderedDays.stream()
                .filter(day -> day.date().isBefore(today))
                .filter(day -> !day.registered())
                .map(DailyCalories::date)
                .toList();

        return new ProgressReportResponse(
                new ReportPeriod(from, to),
                toCurrentDiet(currentDiet),
                dailyCalories,
                weeklyAverages,
                comparison,
                trend,
                daysWithoutMeals);
    }

    /**
     * Valida a ordem, o limite futuro e a duração máxima do período.
     *
     * @param from primeira data incluída
     * @param to última data incluída
     * @param today data atual usada como limite
     */
    private void validatePeriod(LocalDate from, LocalDate to, LocalDate today) {
        if (from.isAfter(to)) {
            throw new BusinessException("'from' must be before or equal to 'to'");
        }
        if (to.isAfter(today)) {
            throw new BusinessException("'to' cannot be in the future");
        }
        if (from.plusDays(MAX_PERIOD_DAYS - 1L).isBefore(to)) {
            throw new BusinessException("Report period cannot exceed " + MAX_PERIOD_DAYS + " days");
        }
    }

    /**
     * Monta o indicador diário com consumo, meta, diferença e quantidade de
     * refeições.
     *
     * @param date data de referência
     * @param calories calorias agregadas no dia
     * @param mealCount quantidade de refeições
     * @param target meta diária ou nula
     * @return indicador diário
     */
    private DailyCalories toDailyCalories(
            LocalDate date,
            BigDecimal calories,
            long mealCount,
            BigDecimal target) {
        BigDecimal normalizedCalories = calories.setScale(2, RoundingMode.HALF_UP);
        return new DailyCalories(
                date,
                normalizedCalories,
                target,
                target == null ? null : normalizedCalories.subtract(target).setScale(2, RoundingMode.HALF_UP),
                mealCount,
                mealCount > 0);
    }

    /**
     * Agrupa os indicadores diários por semana iniciada na segunda-feira.
     *
     * @param days indicadores diários em ordem cronológica
     * @param target meta diária ou nula
     * @return médias semanais em ordem cronológica
     */
    private List<WeeklyAverage> buildWeeklyAverages(
            SequencedCollection<DailyCalories> days,
            BigDecimal target) {
        Map<LocalDate, List<DailyCalories>> byWeek = days.stream()
                .collect(Collectors.groupingBy(
                        day -> day.date().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
                        LinkedHashMap::new,
                        Collectors.mapping(Function.identity(), Collectors.toList())));

        return byWeek.entrySet().stream()
                .map(entry -> {
                    List<DailyCalories> weekDays = entry.getValue();
                    BigDecimal average = average(weekDays);
                    return new WeeklyAverage(
                            entry.getKey(),
                            weekDays.getLast().date(),
                            average,
                            target,
                            percentage(average, target));
                })
                .toList();
    }

    /**
     * Compara a média diária do período completo com a meta vigente.
     *
     * @param days indicadores diários do período
     * @param target meta diária ou nula
     * @return comparação consolidada
     */
    private GoalComparison buildGoalComparison(
            SequencedCollection<DailyCalories> days,
            BigDecimal target) {
        BigDecimal average = average(days);
        return new GoalComparison(
                target,
                average,
                target == null ? null : average.subtract(target).setScale(2, RoundingMode.HALF_UP),
                percentage(average, target));
    }

    /**
     * Compara as duas últimas janelas completas de sete dias encerrados.
     *
     * A classificação considera se a média atual se aproximou ou se afastou da
     * meta em relação à média anterior.
     *
     * @param days indicadores diários em ordem cronológica
     * @param target meta diária ou nula
     * @param today data atual usada para excluir o dia ainda em andamento
     * @return tendência calculada ou indicação de dados insuficientes
     */
    private ProgressTrend buildTrend(
            SequencedCollection<DailyCalories> days,
            BigDecimal target,
            LocalDate today) {
        List<DailyCalories> completedDays = days.stream()
                .filter(day -> day.date().isBefore(today))
                .toList();

        int requiredDays = TREND_WINDOW_DAYS * 2;
        if (target == null || completedDays.size() < requiredDays) {
            return new ProgressTrend("INSUFFICIENT_DATA", null, null, null);
        }

        int size = completedDays.size();
        List<DailyCalories> previousWindow = completedDays.subList(
                size - requiredDays,
                size - TREND_WINDOW_DAYS);
        List<DailyCalories> currentWindow = completedDays.subList(
                size - TREND_WINDOW_DAYS,
                size);

        BigDecimal previousAverage = average(previousWindow);
        BigDecimal currentAverage = average(currentWindow);
        BigDecimal previousDistance = previousAverage.subtract(target).abs();
        BigDecimal currentDistance = currentAverage.subtract(target).abs();
        BigDecimal distanceChange = currentDistance.subtract(previousDistance);

        String status;
        if (distanceChange.abs().compareTo(TREND_LIMITS.get("stableDistance")) <= 0) {
            status = "STABLE";
        } else if (distanceChange.signum() < 0) {
            status = "APPROACHING_GOAL";
        } else {
            status = "MOVING_AWAY_FROM_GOAL";
        }

        return new ProgressTrend(
                status,
                previousAverage,
                currentAverage,
                percentageChange(previousAverage, currentAverage));
    }

    /**
     * Calcula a média calórica de uma coleção de indicadores diários.
     *
     * @param days indicadores usados no cálculo
     * @return média com duas casas decimais
     */
    private BigDecimal average(Collection<DailyCalories> days) {
        if (days.isEmpty()) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        BigDecimal total = days.stream()
                .map(DailyCalories::consumedCalories)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.divide(BigDecimal.valueOf(days.size()), 2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula quanto um valor representa percentualmente de uma meta.
     *
     * @param value valor observado
     * @param target valor de referência
     * @return percentual calculado ou nulo quando não há meta válida
     */
    private BigDecimal percentage(BigDecimal value, BigDecimal target) {
        if (target == null || target.signum() == 0) {
            return null;
        }
        return value.multiply(BigDecimal.valueOf(100))
                .divide(target, 2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula a variação percentual entre um valor anterior e um valor atual.
     *
     * @param previous valor anterior
     * @param current valor atual
     * @return variação percentual ou nula quando o valor anterior é zero
     */
    private BigDecimal percentageChange(BigDecimal previous, BigDecimal current) {
        if (previous.signum() == 0) {
            return null;
        }
        return current.subtract(previous)
                .multiply(BigDecimal.valueOf(100))
                .divide(previous, 2, RoundingMode.HALF_UP);
    }

    /**
     * Converte a dieta vigente para a representação resumida do relatório.
     *
     * @param diet dieta vigente ou nula
     * @return resumo da dieta ou nulo
     */
    private CurrentDiet toCurrentDiet(DietModel diet) {
        if (diet == null) {
            return null;
        }
        return new CurrentDiet(
                diet.getIdDiet(),
                diet.getName(),
                diet.getDailyCalorieTarget(),
                diet.getInitialDate(),
                diet.getFinalDate());
    }
}
