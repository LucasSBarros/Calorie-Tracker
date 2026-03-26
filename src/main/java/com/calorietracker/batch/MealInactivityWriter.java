package com.calorietracker.batch;

import java.util.List;

import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.calorietracker.email.EmailService;
import com.calorietracker.models.NotificationLogModel;
import com.calorietracker.repositories.NotificationLogRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MealInactivityWriter implements ItemWriter<MealReminderItem> {

    private final EmailService emailService;
    private final NotificationLogRepository notificationLogRepository;

    /**
     * Executa a etapa de escrita do batch, responsável por enviar os emails
     * de lembrete e registrar o envio no banco de dados.
     * 
     * Para cada item processado:
     * - envia o email de notificação ao usuário
     * - cria um registro de log com as informações do envio
     * 
     * Ao final, persiste todos os logs em lote.
     * 
     * @param chunk conjunto de itens contendo os usuários a serem notificados
     */
    @Override
    public void write(Chunk<? extends MealReminderItem> chunk) {
        List<NotificationLogModel> logs = chunk.getItems().stream().map(item -> {
            emailService.sendMealReminder(item.user());

            NotificationLogModel log = new NotificationLogModel();
            log.setUser(item.user());
            log.setType(item.type());
            log.setSentAt(item.sentAt());

            return log;
        }).toList();

        notificationLogRepository.saveAll(logs);
    }
}