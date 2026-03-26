package com.calorietracker.scheduler;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Value("${app.reminder.cron}")
    private String reminderCron;

    /**
     * Define o JobDetail do Quartz responsável por executar o job
     * de verificação de inatividade de refeições.
     * 
     * O job é configurado como durável, permitindo que exista mesmo
     * sem triggers associados.
     * 
     * @return JobDetail configurado para o job de inatividade
     */
    @Bean
    public JobDetail mealInactivityJobDetail() {
        return JobBuilder.newJob(MealInactivityQuartzJob.class)
                .withIdentity("mealInactivityJob")
                .storeDurably()
                .build();
    }

    /**
     * Define o Trigger do Quartz responsável por agendar a execução
     * periódica do job de inatividade de refeições.
     * 
     * O agendamento é baseado em expressão cron configurada na aplicação.
     * 
     * @return Trigger configurado com a expressão cron
     */
    @Bean
    public Trigger mealInactivityTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(mealInactivityJobDetail())
                .withIdentity("mealInactivityTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(reminderCron))
                .build();
    }
}