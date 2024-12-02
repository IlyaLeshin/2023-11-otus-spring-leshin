package ru.otus.hw.datamigration.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;


@SuppressWarnings("unused")
@Configuration
public class JobConfig {

    public static final String JOB_NAME = "migrateLibraryDbJob";

    private final Logger logger = LoggerFactory.getLogger("Batch");


    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;


    @Bean
    public Job migrationLibraryDbJob(
            Step migrateAuthorStep,
            Step migrateBookStep,
            Step migrateGenreStep,
            Step migrateCommentStep,
            JobRepository jobRepository
    ) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(splitFlow(migrateAuthorStep, migrateGenreStep))
                .next(migrateBookStep)
                .next(migrateCommentStep)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@NonNull JobExecution jobExecution) {
                        logger.info("Начало job");
                    }

                    @Override
                    public void afterJob(@NonNull JobExecution jobExecution) {
                        logger.info("Конец job");
                    }
                })
                .build();
    }


    @Bean
    public Flow splitFlow(Step migrateAuthorStep, Step migrateGenreStep) {
        return new FlowBuilder<SimpleFlow>("splitFlow")
                .split(taskExecutor())
                .add(flow1(migrateAuthorStep), flow2(migrateGenreStep))
                .build();

    }

    @Bean
    public Flow flow1(Step migrateAuthorStep) {
        return new FlowBuilder<SimpleFlow>("flow1")
                .start(migrateAuthorStep)
                .build();
    }

    @Bean
    public Flow flow2(Step migrateGenreStep) {
        return new FlowBuilder<SimpleFlow>("flow2")
                .start(migrateGenreStep)
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }
}
