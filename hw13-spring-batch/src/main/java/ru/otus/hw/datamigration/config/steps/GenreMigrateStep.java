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
import ru.otus.hw.datamigration.models.MigrationGenre;
import ru.otus.hw.models.Genre;

import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
public class GenreMigrateStep {
    public static final String ITEM_READER_NAME = "genreMongoReader";

    public static final String STEP_NAME = "genreMigrateStep";

    private static final int CHUNK_SIZE = 5;

    private final Logger logger = LoggerFactory.getLogger("GenreMigrateStep");

    @Bean
    public MongoPagingItemReader<Genre> genreMongoReader(MongoTemplate mongoTemplate) {
        return new MongoPagingItemReaderBuilder<Genre>()
                .name(ITEM_READER_NAME)
                .template(mongoTemplate)
                .jsonQuery("{}")
                .targetType(Genre.class)
                .sorts(new HashMap<>())
                .build();
    }


    @Bean
    public JpaItemWriter<MigrationGenre> genreWriter(EntityManager entityManager) {
        return new JpaItemWriterBuilder<MigrationGenre>()
                .entityManagerFactory(entityManager.getEntityManagerFactory())
                .build();
    }


    @Bean
    public Step migrateGenreStep(ItemReader<Genre> genreMongoItemReader,
                                 JpaItemWriter<MigrationGenre> genreWriter,
                                 ItemProcessor<Genre, MigrationGenre> processor,
                                 PlatformTransactionManager platformTransactionManager,
                                 JobRepository jobRepository
    ) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<Genre, MigrationGenre>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(genreMongoItemReader)
                .processor(processor)
                .writer(genreWriter)
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