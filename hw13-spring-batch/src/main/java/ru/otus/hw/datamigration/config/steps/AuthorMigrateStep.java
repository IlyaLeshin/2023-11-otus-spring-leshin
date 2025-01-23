package ru.otus.hw.datamigration.config.steps;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoPagingItemReader;
import org.springframework.batch.item.data.builder.MongoPagingItemReaderBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.datamigration.dto.AuthorMigrationDto;
import ru.otus.hw.models.Author;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AuthorMigrateStep {
    public static final String ITEM_READER_NAME = "authorMongoReader";

    public static final String STEP_NAME = "authorMigrateStep";

    public static final String CREATE_TEMP_TABLE_NAME = "createAuthorsTempTable";

    public static final String DROP_TEMP_TABLE_NAME = "dropAuthorsTempTable";

    private static final int CHUNK_SIZE = 10;

    private final Logger logger = LoggerFactory.getLogger("AuthorMigrateStep");

    @Bean
    public MongoPagingItemReader<Author> authorMongoReader(MongoTemplate mongoTemplate) {
        return new MongoPagingItemReaderBuilder<Author>()
                .name(ITEM_READER_NAME)
                .template(mongoTemplate)
                .jsonQuery("{}")
                .targetType(Author.class)
                .sorts(new HashMap<>())
                .build();
    }


    @Bean
    public TaskletStep createAuthorsTempTable(DataSource dataSource,
                                              PlatformTransactionManager platformTransactionManager,
                                              JobRepository jobRepository
    ) {
        return new StepBuilder(CREATE_TEMP_TABLE_NAME, jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute("""
                            create table if not exists temp_mapping_authors_ids (
                            id_original varchar(255) not null unique,
                            id_migration bigserial not null unique
                            )
                            """);
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

    @Bean
    public CompositeItemWriter<AuthorMigrationDto> compositeAuthorItemWriter(
            JdbcBatchItemWriter<AuthorMigrationDto> authorsIdsWriter,
            JdbcBatchItemWriter<AuthorMigrationDto> authorsWriter) {
        return new CompositeItemWriter<>(List.of(authorsIdsWriter, authorsWriter));
    }

    @Bean
    public JdbcBatchItemWriter<AuthorMigrationDto> authorsIdsWriter(DataSource dataSource) {
        JdbcBatchItemWriter<AuthorMigrationDto> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("""
                insert into temp_mapping_authors_ids (id_original, id_migration)
                values (:id, select next value for author_seq)
                """);
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public JdbcBatchItemWriter<AuthorMigrationDto> authorsWriter(DataSource dataSource) {
        JdbcBatchItemWriter<AuthorMigrationDto> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("""
                insert into authors (id, full_name)
                values (select id_migration from temp_mapping_authors_ids where id_original = :id, :fullName)
                """);
        writer.setDataSource(dataSource);
        return writer;
    }


    @Bean
    public TaskletStep dropAuthorsTempTable(DataSource dataSource,
                                            PlatformTransactionManager platformTransactionManager,
                                            JobRepository jobRepository
    ) {
        return new StepBuilder(DROP_TEMP_TABLE_NAME, jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute("drop table temp_mapping_authors_ids");
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

    @Bean
    public Step migrateAuthorStep(ItemReader<Author> authorMongoReader,
                                  CompositeItemWriter<AuthorMigrationDto> compositeItemWriter,
                                  ItemProcessor<Author, AuthorMigrationDto> processor,
                                  PlatformTransactionManager platformTransactionManager,
                                  JobRepository jobRepository
    ) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<Author, AuthorMigrationDto>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(authorMongoReader)
                .processor(processor)
                .writer(compositeItemWriter)
                .listener(new ChunkListener() {
                    public void beforeChunk(@NonNull ChunkContext chunkContext) {
                        logger.info("Начало пачки");
                    }

                    public void afterChunk(@NonNull ChunkContext chunkContext) {
                        logger.info("Конец пачки");
                    }

                    public void afterChunkError(@NonNull ChunkContext chunkContext) {
                        logger.info("Ошибка пачки");
                    }
                })
                .build();
    }
}