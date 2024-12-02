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
import ru.otus.hw.datamigration.models.MigrationBook;
import ru.otus.hw.models.Book;

import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
public class BookMigrateStep {
    public static final String ITEM_READER_NAME = "bookMongoReader";

    public static final String STEP_NAME = "bookMigrateStep";

    private static final int CHUNK_SIZE = 15;

    private final Logger logger = LoggerFactory.getLogger("BookMigrateStep");

    @Bean
    public MongoPagingItemReader<Book> bookMongoReader(MongoTemplate mongoTemplate) {
        return new MongoPagingItemReaderBuilder<Book>()
                .name(ITEM_READER_NAME)
                .template(mongoTemplate)
                .jsonQuery("{}")
                .targetType(Book.class)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public JpaItemWriter<MigrationBook> bookWriter(EntityManager entityManager) {
        return new JpaItemWriterBuilder<MigrationBook>()
                .entityManagerFactory(entityManager.getEntityManagerFactory())
                .build();
    }


    @Bean
    public Step migrateBookStep(ItemReader<Book> bookMongoItemReader,
                                JpaItemWriter<MigrationBook> bookWriter,
                                ItemProcessor<Book, MigrationBook> processor,
                                PlatformTransactionManager platformTransactionManager,
                                JobRepository jobRepository
    ) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<Book, MigrationBook>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(bookMongoItemReader)
                .processor(processor)
                .writer(bookWriter)
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