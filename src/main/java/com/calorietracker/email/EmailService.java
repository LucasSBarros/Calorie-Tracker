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

    /**
     * Envia uma notificação por email informando ao usuário que um novo plano de
     * dieta foi publicado.
     * A mensagem contém o nome do usuário, o nome da dieta e o identificador do
     * plano para facilitar a identificação dentro da aplicação. O objetivo dessa
     * notificação é alertar o usuário de que existe um novo plano alimentar
     * disponível para consulta.
     *
     * @param to       endereço de email do destinatário que receberá a notificação
     * @param userName nome do usuário que receberá o plano de dieta
     * @param dietName nome do plano de dieta que foi publicado
     * @param dietId   identificador único do plano de dieta criado
     */
    public void sendDietPublishedNotification(String to, String userName, String dietName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("Novo plano de dieta disponível");
        message.setText(
                "Olá, " + userName + "!\n\n" +
                        "Um novo plano de dieta foi publicado para você.\n" +
                        "Plano: " + dietName + "\n\n" +
                        "Acesse a aplicação para visualizar os detalhes.");

        mailSender.send(message);
    }
}