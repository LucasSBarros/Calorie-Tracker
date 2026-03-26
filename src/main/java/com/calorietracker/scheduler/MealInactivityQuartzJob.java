package com.calorietracker.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.stereotype.Component;

import com.calorietracker.exceptions.BatchSchedulingException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MealInactivityQuartzJob implements Job {

    private final JobOperator jobOperator;
    private final org.springframework.batch.core.job.Job mealInactivityJob;

    /**
     * Executa o job de verificação de inatividade de refeições através do Quartz.
     * 
     * Este método é acionado pelo scheduler conforme a configuração de cron,
     * iniciando o job do Spring Batch com parâmetros únicos para cada execução.
     * 
     * Em caso de erro na execução do job, lança uma exceção específica de
     * agendamento.
     * 
     * @param context contexto de execução do Quartz
     * @throws JobExecutionException exceção lançada quando ocorre falha na execução
     *                               do job
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            jobOperator.start(
                    mealInactivityJob,
                    new JobParametersBuilder()
                            .addLong("timestamp", System.currentTimeMillis())
                            .toJobParameters());
        } catch (Exception ex) {
            throw new JobExecutionException(
                    new BatchSchedulingException("Error executing meal inactivity batch job", ex));
        }
    }
}