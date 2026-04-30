package com.calorietracker.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.calorietracker.dtos.DietReportDto;
import com.calorietracker.dtos.MealIngredientReportDto;
import com.calorietracker.dtos.MealReportDto;
import com.calorietracker.dtos.UserReportDataDto;
import com.calorietracker.exceptions.PdfReportGenerationException;
import com.calorietracker.services.ReportQueryService;
import com.calorietracker.services.ReportService;
import org.xhtmlrenderer.pdf.ITextRenderer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportQueryService reportQueryService;

    /**
     * Gera o relatório nutricional em PDF do usuário.
     * 
     * @param userId
     * @return arquivo PDF em bytes
     */
    @Override
    @Transactional(readOnly = true)
    public byte[] generateUserReportPdf(UUID userId) {
        UserReportDataDto report = reportQueryService.getUserReportData(userId);

        String html = loadTemplate("reports/user-report.xhtml");
        String css = loadTemplate("reports/report.css");

        html = html.replace(
                "<link rel=\"stylesheet\" href=\"report.css\" />",
                "<style>" + css + "</style>");

        html = fillTemplate(html, report);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);

            return outputStream.toByteArray();
        } catch (Exception ex) {
            throw new PdfReportGenerationException("Error generating PDF report", ex);
        }
    }

    /**
     * Lê um arquivo do classpath e retorna seu conteúdo em texto.
     * 
     * @param path
     * @return conteúdo do arquivo
     */
    private String loadTemplate(String path) {
        try (InputStream inputStream = new ClassPathResource(path).getInputStream()) {
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
    private String fillTemplate(String html, UserReportDataDto report) {
        return html
                .replace("{{name}}", escape(value(report.name())))
                .replace("{{email}}", escape(value(report.email())))
                .replace("{{weight}}", escape(formatWithSuffix(report.weight(), " g")))
                .replace("{{height}}", escape(formatWithSuffix(report.height(), " cm")))
                .replace("{{birthDate}}", escape(value(report.birthDate())))
                .replace("{{age}}", escape(String.valueOf(report.age())))
                .replace("{{gender}}", escape(value(report.gender())))
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
     * Monta a lista XHTML de dietas do usuário.
     * 
     * @param diets
     * @return lista de dietas formatada em XHTML
     */
    private String buildDietList(List<DietReportDto> diets) {
        if (diets == null || diets.isEmpty()) {
            return "<li>Nenhuma dieta cadastrada</li>";
        }

        StringBuilder result = new StringBuilder();

        for (DietReportDto diet : diets) {
            result.append("<li>")
                    .append("<strong>Nome:</strong> ").append(escape(value(diet.name()))).append("<br />")
                    .append("<strong>Calorias totais:</strong> ").append(escape(value(diet.totalCalories())))
                    .append("<br />")
                    .append("<strong>Data inicial:</strong> ").append(escape(value(diet.initialDate())))
                    .append("<br />")
                    .append("<strong>Data final:</strong> ").append(escape(value(diet.finalDate())));

            if (diet.meals() != null && !diet.meals().isEmpty()) {
                result.append("<ul>");

                for (MealReportDto meal : diet.meals()) {
                    result.append("<li>")
                            .append("<strong>Refeição:</strong> ").append(escape(value(meal.description())))
                            .append("<br />")
                            .append("<strong>Data/Hora:</strong> ").append(escape(value(meal.mealDateTime())))
                            .append("<br />")
                            .append("<strong>Calorias:</strong> ").append(escape(value(meal.totalCaloriesPerMeal())));

                    if (meal.mealIngredients() != null && !meal.mealIngredients().isEmpty()) {
                        result.append("<ul>");

                        for (MealIngredientReportDto item : meal.mealIngredients()) {
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