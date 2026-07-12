package com.calorietracker.dtos.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Consolida consumo calórico, aderência à meta e tendência do usuário.
 *
 * @param period período considerado no relatório
 * @param currentDiet dieta vigente na data de geração
 * @param dailyCalories consumo agregado por dia
 * @param weeklyAverages médias agrupadas por semana
 * @param goalComparison comparação geral com a meta calórica
 * @param trend tendência calculada pelas duas últimas janelas completas
 * @param daysWithoutMeals dias encerrados sem refeição registrada
 */
public record ProgressReportResponse(
        ReportPeriod period,
        CurrentDiet currentDiet,
        List<DailyCalories> dailyCalories,
        List<WeeklyAverage> weeklyAverages,
        GoalComparison goalComparison,
        ProgressTrend trend,
        List<LocalDate> daysWithoutMeals) {

    /**
     * Delimita as datas incluídas no relatório.
     *
     * @param from primeira data incluída
     * @param to última data incluída
     */
    public record ReportPeriod(LocalDate from, LocalDate to) {
    }

    /**
     * Resume a dieta vigente e sua meta diária.
     *
     * @param id identificador da dieta
     * @param name nome da dieta
     * @param dailyCalorieTarget meta diária de calorias
     * @param initialDate início da vigência
     * @param finalDate fim da vigência
     */
    public record CurrentDiet(
            UUID id,
            String name,
            BigDecimal dailyCalorieTarget,
            LocalDate initialDate,
            LocalDate finalDate) {
    }

    /**
     * Apresenta o consumo consolidado de um dia.
     *
     * @param date data de referência
     * @param consumedCalories calorias consumidas
     * @param targetCalories meta calórica aplicada
     * @param differenceCalories diferença entre consumo e meta
     * @param mealCount quantidade de refeições registradas
     * @param registered indica se houve ao menos uma refeição
     */
    public record DailyCalories(
            LocalDate date,
            BigDecimal consumedCalories,
            BigDecimal targetCalories,
            BigDecimal differenceCalories,
            long mealCount,
            boolean registered) {
    }

    /**
     * Apresenta a média de consumo dos dias incluídos em uma semana.
     *
     * @param weekStart primeiro dia da semana
     * @param weekEnd último dia da semana incluído no relatório
     * @param averageCalories média diária de calorias
     * @param targetCalories meta diária de calorias
     * @param adherencePercentage percentual da média em relação à meta
     */
    public record WeeklyAverage(
            LocalDate weekStart,
            LocalDate weekEnd,
            BigDecimal averageCalories,
            BigDecimal targetCalories,
            BigDecimal adherencePercentage) {
    }

    /**
     * Resume a comparação do período completo com a meta calórica.
     *
     * @param targetCalories meta diária de calorias
     * @param averageConsumedCalories média diária consumida
     * @param differenceCalories diferença entre média e meta
     * @param adherencePercentage percentual da média em relação à meta
     */
    public record GoalComparison(
            BigDecimal targetCalories,
            BigDecimal averageConsumedCalories,
            BigDecimal differenceCalories,
            BigDecimal adherencePercentage) {
    }

    /**
     * Descreve a evolução das médias de duas janelas consecutivas de sete dias.
     *
     * @param status classificação da tendência
     * @param previousSevenDayAverage média da janela anterior
     * @param currentSevenDayAverage média da janela atual
     * @param variationPercentage variação percentual entre as médias
     */
    public record ProgressTrend(
            String status,
            BigDecimal previousSevenDayAverage,
            BigDecimal currentSevenDayAverage,
            BigDecimal variationPercentage) {
    }
}
