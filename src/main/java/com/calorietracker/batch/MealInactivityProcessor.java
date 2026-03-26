package com.calorietracker.batch;

import java.time.LocalDateTime;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.calorietracker.models.UserModel;
import com.calorietracker.repositories.NotificationLogRepository;

import lombok.RequiredArgsConstructor;

@Component
@StepScope
@RequiredArgsConstructor
public class MealInactivityProcessor implements ItemProcessor<UserModel, MealReminderItem> {

    private static final String TYPE = "MEAL_INACTIVITY";

    private final NotificationLogRepository notificationLogRepository;

    @Value("${app.reminder.inactivity-hours}")
    private long inactivityHours;

    /**
     * Processa um usuário verificando se ele já recebeu recentemente
     * um alerta de inatividade de refeições.
     * 
     * Caso não tenha recebido, cria um item contendo as informações
     * necessárias para envio do email e registro do log.
     * 
     * @param user usuário a ser avaliado
     * @return MealReminderItem com dados para notificação ou null caso já tenha
     *         sido notificado recentemente
     */
    @Override
    public MealReminderItem process(UserModel user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limit = now.minusHours(inactivityHours);

        boolean alreadySentRecently = notificationLogRepository
                .existsByUser_IdUserAndTypeAndSentAtAfter(user.getIdUser(), TYPE, limit);

        MealReminderItem result = null;

        if (!alreadySentRecently) {
            result = new MealReminderItem(user, now, TYPE);
        }

        return result;
    }
}