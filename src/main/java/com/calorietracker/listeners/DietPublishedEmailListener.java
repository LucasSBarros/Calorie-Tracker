package com.calorietracker.listeners;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.calorietracker.email.EmailService;
import com.calorietracker.events.DietPublishedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DietPublishedEmailListener {

    private final EmailService emailService;

    /**
     * Trata o evento de publicação de uma nova dieta. Este método é executado
     * de forma assíncrona após a confirmação da transação que persistiu a dieta
     * no banco de dados. Ao receber o evento, ele aciona o serviço de email para
     * enviar uma notificação ao usuário informando que um novo plano de dieta
     * foi publicado para ele.
     *
     * @param event evento que contém as informações da dieta publicada e os dados
     *              do usuário que deve ser notificado
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(DietPublishedEvent event) {
        emailService.sendDietPublishedNotification(
                event.userEmail(),
                event.userName(),
                event.dietName());
    }
}