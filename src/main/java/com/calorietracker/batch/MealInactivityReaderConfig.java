package com.calorietracker.batch;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.calorietracker.models.UserModel;
import com.calorietracker.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MealInactivityReaderConfig {

    private final UserRepository userRepository;

    /**
     * Cria o reader responsável por buscar usuários que não registraram
     * refeições dentro do período configurado.
     * 
     * O reader consulta o banco de dados considerando o tempo de inatividade
     * e retorna uma lista de usuários elegíveis para notificação.
     * 
     * @param inactivityHours quantidade de horas sem registro de refeições
     * @return ListItemReader contendo os usuários inativos
     */
    @Bean
    @StepScope
    public ListItemReader<UserModel> mealInactivityReader(
            @Value("${app.reminder.inactivity-hours}") long inactivityHours) {

        LocalDateTime limit = LocalDateTime.now().minusHours(inactivityHours);
        List<UserModel> users = userRepository.findUsersWithoutMealsSince(limit);

        return new ListItemReader<>(users);
    }
}