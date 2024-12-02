package ru.otus.hw.datamigration.config.steps;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoPagingItemReader;
import org.springframework.batch.item.data.builder.MongoPagingItemReaderBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.datamigration.models.MigrationAuthor;
import ru.otus.hw.models.Author;

import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
public class AuthorMigrateStep {
    public static final String ITEM_READER_NAME = "authorMongoReader";

    public static final String STEP_NAME = "authorMigrateStep";

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
    public JpaItemWriter<MigrationAuthor> authorWriter(EntityManager entityManager) {
        return new JpaItemWriterBuilder<MigrationAuthor>()
                .entityManagerFactory(entityManager.getEntityManagerFactory())
                .build();
    }

    @Bean
    public Step migrateAuthorStep(ItemReader<Author> authorMongoReader,
                                  JpaItemWriter<MigrationAuthor> authorWriter,
                                  ItemProcessor<Author, MigrationAuthor> processor,
                                  PlatformTransactionManager platformTransactionManager,
                                  JobRepository jobRepository
    ) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<Author, MigrationAuthor>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(authorMongoReader)
                .processor(processor)
                .writer(authorWriter)
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
