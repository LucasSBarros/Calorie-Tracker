package com.calorietracker.services.impl;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.calorietracker.dtos.summary.DietSummaryResponse;
import com.calorietracker.dtos.summary.MealIngredientSummaryResponse;
import com.calorietracker.dtos.summary.MealSummaryResponse;
import com.calorietracker.dtos.report.ProgressReportResponse;
import com.calorietracker.dtos.report.ProgressReportResponse.DailyCalories;
import com.calorietracker.dtos.report.ProgressReportResponse.WeeklyAverage;
import com.calorietracker.dtos.report.UserReportData;
import com.calorietracker.exceptions.PdfReportGenerationException;
import com.calorietracker.models.Gender;
import com.calorietracker.services.ProgressReportService;
import com.calorietracker.services.ReportQueryService;
import com.calorietracker.services.ReportService;
import org.xhtmlrenderer.pdf.ITextRenderer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final ReportQueryService reportQueryService;
    private final ProgressReportService progressReportService;

    /**
     * Gera o relatório nutricional em PDF do usuário.
     * 
     * @param userId
     * @return arquivo PDF em bytes
     */
    @Override
    @Transactional(readOnly = true)
    public byte[] generateUserReportPdf(UUID userId) {
        var report = reportQueryService.getUserReportData(userId);

        var html = loadTemplate("reports/user-report.xhtml");
        var css = loadTemplate("reports/report.css");

        html = html.replace(
                "<link rel=\"stylesheet\" href=\"report.css\" />",
                "<style>" + css + "</style>");

        html = fillTemplate(html, report);

        return renderPdf(html);
    }

    /**
     * Gera o relatório de progresso calórico em PDF.
     *
     * @param userEmail e-mail do usuário autenticado
     * @param from primeira data incluída ou nula
     * @param to última data incluída ou nula
     * @return arquivo PDF em bytes
     */
    @Override
    @Transactional(readOnly = true)
    public byte[] generateProgressReportPdf(String userEmail, LocalDate from, LocalDate to) {
        var report = progressReportService.generate(userEmail, from, to);

        var html = loadTemplate("reports/progress-report.xhtml");
        var css = loadTemplate("reports/progress-report.css");

        html = html.replace(
                "<link rel=\"stylesheet\" href=\"progress-report.css\" />",
                "<style>" + css + "</style>");

        html = fillProgressTemplate(html, userEmail, report);

        return renderPdf(html);
    }

    /**
     * Converte o conteúdo XHTML preenchido em um arquivo PDF.
     *
     * @param html conteúdo XHTML completo
     * @return arquivo PDF em bytes
     */
    private byte[] renderPdf(String html) {
        try (var outputStream = new ByteArrayOutputStream()) {
            var renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);

            return outputStream.toByteArray();
        } catch (Exception ex) {
            throw new PdfReportGenerationException("Error generating PDF report", ex);
        }
    }

    /**
     * Substitui os placeholders do relatório de progresso.
     *
     * @param html template XHTML
     * @param userEmail e-mail do usuário autenticado
     * @param report dados consolidados do relatório
     * @return conteúdo XHTML preenchido
     */
    private String fillProgressTemplate(
            String html,
            String userEmail,
            ProgressReportResponse report) {
        var diet = report.currentDiet();
        var comparison = report.goalComparison();
        var trend = report.trend();

        return html
                .replace("{{userEmail}}", escape(value(userEmail)))
                .replace("{{periodFrom}}", escape(formatDate(report.period().from())))
                .replace("{{periodTo}}", escape(formatDate(report.period().to())))
                .replace("{{generatedAt}}", escape(formatDate(LocalDate.now())))
                .replace("{{dietName}}", escape(diet == null ? "-" : value(diet.name())))
                .replace("{{dietPeriod}}", escape(formatDietPeriod(report)))
                .replace("{{dailyTarget}}", escape(formatCalories(
                        diet == null ? null : diet.dailyCalorieTarget())))
                .replace("{{averageConsumed}}", escape(formatCalories(
                        comparison.averageConsumedCalories())))
                .replace("{{goalDifference}}", escape(formatSignedCalories(
                        comparison.differenceCalories())))
                .replace("{{adherencePercentage}}", escape(formatPercentage(
                        comparison.adherencePercentage())))
                .replace("{{trendStatus}}", escape(translateTrendStatus(trend.status())))
                .replace("{{previousAverage}}", escape(formatCalories(
                        trend.previousSevenDayAverage())))
                .replace("{{currentAverage}}", escape(formatCalories(
                        trend.currentSevenDayAverage())))
                .replace("{{trendVariation}}", escape(formatPercentage(
                        trend.variationPercentage())))
                .replace("{{weeklyRows}}", buildWeeklyRows(report.weeklyAverages()))
                .replace("{{dailyRows}}", buildDailyRows(report.dailyCalories()))
                .replace("{{daysWithoutMeals}}", buildDaysWithoutMeals(report));
    }

    /**
     * Monta as linhas da tabela de médias semanais.
     *
     * @param averages médias semanais
     * @return linhas XHTML
     */
    private String buildWeeklyRows(List<WeeklyAverage> averages) {
        if (averages == null || averages.isEmpty()) {
            return "<tr><td colspan=\"5\">Nenhuma média semanal disponível</td></tr>";
        }

        var result = new StringBuilder();
        for (WeeklyAverage average : averages) {
            result.append("<tr>")
                    .append("<td>").append(escape(formatDate(average.weekStart()))).append("</td>")
                    .append("<td>").append(escape(formatDate(average.weekEnd()))).append("</td>")
                    .append("<td>").append(escape(formatCalories(average.averageCalories()))).append("</td>")
                    .append("<td>").append(escape(formatCalories(average.targetCalories()))).append("</td>")
                    .append("<td>").append(escape(formatPercentage(average.adherencePercentage()))).append("</td>")
                    .append("</tr>");
        }
        return result.toString();
    }

    /**
     * Monta as linhas da tabela de consumo diário.
     *
     * @param days consumos diários
     * @return linhas XHTML
     */
    private String buildDailyRows(List<DailyCalories> days) {
        if (days == null || days.isEmpty()) {
            return "<tr><td colspan=\"6\">Nenhum consumo diário disponível</td></tr>";
        }

        var result = new StringBuilder();
        for (DailyCalories day : days) {
            result.append(day.registered() ? "<tr>" : "<tr class=\"missing\">")
                    .append("<td>").append(escape(formatDate(day.date()))).append("</td>")
                    .append("<td>").append(escape(formatCalories(day.consumedCalories()))).append("</td>")
                    .append("<td>").append(escape(formatCalories(day.targetCalories()))).append("</td>")
                    .append("<td>").append(escape(formatSignedCalories(day.differenceCalories()))).append("</td>")
                    .append("<td>").append(day.mealCount()).append("</td>")
                    .append("<td>").append(day.registered() ? "Registrado" : "Sem refeição").append("</td>")
                    .append("</tr>");
        }
        return result.toString();
    }

    /**
     * Monta a lista de dias encerrados sem refeições registradas.
     *
     * @param report dados consolidados
     * @return lista XHTML
     */
    private String buildDaysWithoutMeals(ProgressReportResponse report) {
        if (report.daysWithoutMeals() == null || report.daysWithoutMeals().isEmpty()) {
            return "<li>Nenhum dia sem refeição registrada</li>";
        }

        var result = new StringBuilder();
        for (LocalDate date : report.daysWithoutMeals()) {
            result.append("<li>")
                    .append(escape(formatDate(date)))
                    .append("</li>");
        }
        return result.toString();
    }

    /**
     * Formata o período de vigência da dieta.
     *
     * @param report dados consolidados
     * @return período formatado
     */
    private String formatDietPeriod(ProgressReportResponse report) {
        if (report.currentDiet() == null) {
            return "-";
        }
        return formatDate(report.currentDiet().initialDate())
                + " a "
                + formatDate(report.currentDiet().finalDate());
    }

    /**
     * Traduz a classificação interna da tendência.
     *
     * @param status classificação interna
     * @return descrição em português
     */
    private String translateTrendStatus(String status) {
        if (status == null) {
            return "-";
        }
        return switch (status) {
            case "APPROACHING_GOAL" -> "Aproximando-se da meta";
            case "MOVING_AWAY_FROM_GOAL" -> "Afastando-se da meta";
            case "STABLE" -> "Estável";
            case "INSUFFICIENT_DATA" -> "Dados insuficientes";
            default -> status;
        };
    }

    /**
     * Formata uma data para apresentação no relatório.
     *
     * @param date data a ser formatada
     * @return data formatada ou hífen
     */
    private String formatDate(LocalDate date) {
        return date == null ? "-" : DATE_FORMATTER.format(date);
    }

    /**
     * Formata data e hora para apresentação no relatório.
     *
     * @param dateTime data e hora a serem formatadas
     * @return data e hora formatadas ou hífen
     */
    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime == null ? "-" : DATE_TIME_FORMATTER.format(dateTime);
    }

    /**
     * Formata um valor calórico.
     *
     * @param calories valor em calorias
     * @return valor formatado ou hífen
     */
    private String formatCalories(BigDecimal calories) {
        return calories == null ? "-" : formatDecimal(calories) + " kcal";
    }

    /**
     * Formata uma diferença calórica preservando o sinal positivo.
     *
     * @param calories diferença em calorias
     * @return diferença formatada ou hífen
     */
    private String formatSignedCalories(BigDecimal calories) {
        if (calories == null) {
            return "-";
        }
        String sign = calories.signum() > 0 ? "+" : "";
        return sign + formatDecimal(calories) + " kcal";
    }

    /**
     * Formata um percentual.
     *
     * @param percentage percentual
     * @return percentual formatado ou hífen
     */
    private String formatPercentage(BigDecimal percentage) {
        return percentage == null ? "-" : formatDecimal(percentage) + "%";
    }

    /**
     * Formata um número decimal com duas casas e separador brasileiro.
     *
     * @param number número decimal
     * @return número formatado
     */
    private String formatDecimal(BigDecimal number) {
        return number.setScale(2, java.math.RoundingMode.HALF_UP)
                .toPlainString()
                .replace('.', ',');
    }

    /**
     * Lê um arquivo do classpath e retorna seu conteúdo em texto.
     * 
     * @param path
     * @return conteúdo do arquivo
     */
    private String loadTemplate(String path) {
        try (var inputStream = new ClassPathResource(path).getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new PdfReportGenerationException("Error loading template file: " + path, ex);
        }
    }

    /**
     * Substitui os placeholders do template XHTML pelos dados do relatório.
     * 
     * @param html
     * @param report
     * @return conteúdo XHTML preenchido
     */
    private String fillTemplate(String html, UserReportData report) {
        return html
                .replace("{{name}}", escape(value(report.name())))
                .replace("{{email}}", escape(value(report.email())))
                .replace("{{generatedAt}}", escape(formatDate(LocalDate.now())))
                .replace("{{weight}}", escape(formatWithSuffix(report.weight(), " g")))
                .replace("{{height}}", escape(formatWithSuffix(report.height(), " cm")))
                .replace("{{birthDate}}", escape(formatDate(report.birthDate())))
                .replace("{{age}}", escape(String.valueOf(report.age())))
                .replace("{{gender}}", escape(formatGender(report.gender())))
                .replace("{{imc}}", escape(value(report.imc())))
                .replace("{{tmb}}", escape(value(report.tmb())))
                .replace("{{goalWeight}}", escape(value(report.goalWeight())))
                .replace("{{goalBf}}", escape(value(report.goalBf())))
                .replace("{{currentWeight}}", escape(value(report.currentWeight())))
                .replace("{{currentBf}}", escape(value(report.currentBf())))
                .replace("{{weightProgressPercent}}", escape(formatWithSuffix(report.weightProgressPercent(), "%")))
                .replace("{{bfProgressPercent}}", escape(formatWithSuffix(report.bfProgressPercent(), "%")))
                .replace("{{diets}}", buildDietList(report.diets()));
    }

    /**
     * Traduz o gênero para a descrição apresentada no relatório.
     *
     * @param gender gênero cadastrado
     * @return descrição em português ou hífen quando ausente
     */
    private String formatGender(Gender gender) {
        if (gender == null) {
            return "-";
        }
        return switch (gender) {
            case MALE -> "Masculino";
            case FEMALE -> "Feminino";
        };
    }

    /**
     * Monta a lista XHTML de dietas do usuário.
     * 
     * @param diets
     * @return lista de dietas formatada em XHTML
     */
    private String buildDietList(List<DietSummaryResponse> diets) {
        if (diets == null || diets.isEmpty()) {
            return "<li>Nenhuma dieta cadastrada</li>";
        }

        var result = new StringBuilder();

        for (DietSummaryResponse diet : diets) {
            result.append("<li>")
                    .append("<strong>Nome:</strong> ").append(escape(value(diet.name()))).append("<br />")
                    .append("<strong>Calorias totais:</strong> ").append(escape(value(diet.totalCalories())))
                    .append("<br />")
                    .append("<strong>Data inicial:</strong> ").append(escape(formatDate(diet.initialDate())))
                    .append("<br />")
                    .append("<strong>Data final:</strong> ").append(escape(formatDate(diet.finalDate())));

            if (diet.meals() != null && !diet.meals().isEmpty()) {
                result.append("<ul>");

                for (MealSummaryResponse meal : diet.meals()) {
                    result.append("<li>")
                            .append("<strong>Refeição:</strong> ").append(escape(value(meal.description())))
                            .append("<br />")
                            .append("<strong>Data/Hora:</strong> ").append(escape(formatDateTime(meal.mealDateTime())))
                            .append("<br />")
                            .append("<strong>Calorias:</strong> ").append(escape(value(meal.totalCaloriesPerMeal())));

                    if (meal.mealIngredients() != null && !meal.mealIngredients().isEmpty()) {
                        result.append("<ul>");

                        for (MealIngredientSummaryResponse item : meal.mealIngredients()) {
                            result.append("<li>")
                                    .append(escape(value(item.ingredientName())))
                                    .append(" - ")
                                    .append(escape(value(item.weight())))
                                    .append(" g - ")
                                    .append(escape(value(item.calories())))
                                    .append(" kcal")
                                    .append("</li>");
                        }

                        result.append("</ul>");
                    }

                    result.append("</li>");
                }

                result.append("</ul>");
            }

            result.append("</li>");
        }

        return result.toString();
    }

    /**
     * Formata valores com sufixo.
     * 
     * @param value
     * @param suffix
     * @return valor formatado
     */
    private String formatWithSuffix(Object value, String suffix) {
        return value == null ? "-" : value + suffix;
    }

    /**
     * Retorna o valor como texto ou hífen quando nulo.
     * 
     * @param value
     * @return valor formatado
     */
    private String value(Object value) {
        return value == null ? "-" : String.valueOf(value);
    }

    /**
     * Escapa caracteres especiais para XHTML.
     * 
     * @param value
     * @return texto escapado
     */
    private String escape(String value) {
        return value == null
                ? ""
                : value.replace("&", "&amp;")
                        .replace("<", "&lt;")
                        .replace(">", "&gt;");
    }
}
