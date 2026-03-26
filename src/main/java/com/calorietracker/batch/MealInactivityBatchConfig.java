package com.calorietracker.batch;

import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.calorietracker.models.UserModel;

@Configuration
public class MealInactivityBatchConfig {

    /**
     * Define o step responsável por processar usuários que estão sem registrar
     * refeições.
     * 
     * O step utiliza o modelo chunk, onde:
     * - Reader busca os usuários inativos
     * - Processor filtra e prepara os dados para notificação
     * - Writer envia o email e registra o log da notificação
     * 
     * @param jobRepository           repositório de controle do Spring Batch
     * @param transactionManager      gerenciador de transações do step
     * @param mealInactivityReader    leitor responsável por buscar os usuários
     * @param mealInactivityProcessor processador que aplica a regra de negócio
     * @param mealInactivityWriter    responsável por enviar email e salvar log
     * @return Step configurado para o job de inatividade de refeições
     */
    @Bean
    public Step mealInactivityStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ItemReader<UserModel> mealInactivityReader,
            ItemProcessor<UserModel, MealReminderItem> mealInactivityProcessor,
            ItemWriter<MealReminderItem> mealInactivityWriter) {

        return new StepBuilder("mealInactivityStep", jobRepository)
                .<UserModel, MealReminderItem>chunk(100)
                .reader(mealInactivityReader)
                .processor(mealInactivityProcessor)
                .writer(mealInactivityWriter)
                .transactionManager(transactionManager)
                .build();
    }

    /**
     * Define o job responsável por executar o fluxo de verificação de inatividade
     * de refeições.
     * 
     * O job é composto por um único step que identifica usuários inativos
     * e dispara notificações por email.
     * 
     * @param jobRepository      repositório de controle do Spring Batch
     * @param mealInactivityStep step responsável pelo processamento
     * @return Job configurado para execução do batch de inatividade
     */
    @Bean
    public Job mealInactivityJob(
            JobRepository jobRepository,
            Step mealInactivityStep) {

        return new JobBuilder("mealInactivityJob", jobRepository)
                .start(mealInactivityStep)
                .build();
    }
}