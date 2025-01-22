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
            Step createAuthorsTempTable,
            Step migrateAuthorStep,
            Step dropAuthorsTempTable,
            Step createBooksTempTable,
            Step migrateBookStep,
            Step dropBooksTempTable,
            Step createGenresTempTable,
            Step migrateGenreStep,
            Step dropGenresTempTable,
            Step createCommentsTempTable,
            Step migrateCommentStep,
            Step dropCommentsTempTable,
            JobRepository jobRepository
    ) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(splitFlow(createAuthorsTempTable, migrateAuthorStep,
                        createGenresTempTable, migrateGenreStep))
                .next(createBooksTempTable)
                .next(migrateBookStep)
                .next(createCommentsTempTable)
                .next(migrateCommentStep)
                .next(dropAuthorsTempTable)
                .next(dropGenresTempTable)
                .next(dropBooksTempTable)
                .next(dropCommentsTempTable)
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
    public Flow splitFlow(Step createAuthorsTempTable, Step migrateAuthorStep,
                          Step createGenresTempTable, Step migrateGenreStep) {
        return new FlowBuilder<SimpleFlow>("splitFlow")

                .split(taskExecutor())
                .add(flow1(createAuthorsTempTable, migrateAuthorStep),
                        flow2(createGenresTempTable, migrateGenreStep))
                .build();

    }

    @Bean
    public Flow flow1(Step createAuthorsTempTable, Step migrateAuthorStep) {
        return new FlowBuilder<SimpleFlow>("flow1")
                .start(createAuthorsTempTable)
                .next(migrateAuthorStep)
                .build();
    }

    @Bean
    public Flow flow2(Step createGenresTempTable, Step migrateGenreStep) {
        return new FlowBuilder<SimpleFlow>("flow2")
                .start(createGenresTempTable)
                .next(migrateGenreStep)
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }


}
