package com.calorietracker.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.calorietracker.models.UserModel;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.reminder.from-email}")
    private String fromEmail;

    /**
     * Envia um email de lembrete ao usuário informando sobre a ausência
     * de registro de refeições.
     * 
     * O email contém uma mensagem simples incentivando o usuário a acessar
     * a aplicação e manter o acompanhamento alimentar atualizado.
     * 
     * @param user usuário que receberá o lembrete por email
     */
    public void sendMealReminder(UserModel user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(user.getEmail());
        message.setSubject("Lembrete de registro de refeições");
        message.setText(
                "Olá, " + user.getName() + "!\n\n" +
                        "Percebemos que faz algum tempo que você não registra suas refeições no sistema.\n" +
                        "Acesse a aplicação e mantenha seu acompanhamento alimentar em dia.");
        mailSender.send(message);
    }
}