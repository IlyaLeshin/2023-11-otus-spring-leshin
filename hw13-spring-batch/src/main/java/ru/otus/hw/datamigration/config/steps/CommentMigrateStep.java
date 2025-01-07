package ru.otus.hw.datamigration.config.steps;

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
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.datamigration.cache.MigrationCommentIdCache;
import ru.otus.hw.datamigration.dto.MigrationCommentDto;
import ru.otus.hw.datamigration.repositories.MigrationCommentRepository;
import ru.otus.hw.models.Comment;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
public class CommentMigrateStep {
    public static final String ITEM_READER_NAME = "commentMongoReader";

    public static final String STEP_NAME = "commentMigrateStep";

    private static final int CHUNK_SIZE = 20;

    private final Logger logger = LoggerFactory.getLogger("CommentMigrateStep");

    private final  MigrationCommentRepository migrationCommentRepository;

    private final MigrationCommentIdCache migrationCommentIdCache;

    @Bean
    public MongoPagingItemReader<Comment> commentMongoReader(MongoTemplate mongoTemplate) {
        return new MongoPagingItemReaderBuilder<Comment>()
                .name(ITEM_READER_NAME)
                .template(mongoTemplate)
                .jsonQuery("{}")
                .targetType(Comment.class)
                .sorts(new HashMap<>())
                .build();
    }


    @Bean
    public JdbcBatchItemWriter<MigrationCommentDto> commentWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<MigrationCommentDto>()
                .dataSource(dataSource)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("insert into comments(id, text, book_id) values (:id, :text, :bookId)")
                .build();
    }


    @Bean
    public Step migrateCommentStep(ItemReader<Comment> commentMongoItemReader,
                                   JdbcBatchItemWriter<MigrationCommentDto> commentWriter,
                                   ItemProcessor<Comment, MigrationCommentDto> processor,
                                   PlatformTransactionManager platformTransactionManager,
                                   JobRepository jobRepository
    ) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<Comment, MigrationCommentDto>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(commentMongoItemReader)
                .processor(processor)
                .writer(commentWriter)
                .listener(new ChunkListener() {
                    public void beforeChunk(@NonNull ChunkContext chunkContext) {
                        logger.info("Начало пачки");
                        migrationCommentIdCache.putList(migrationCommentRepository.getNextSequenceRangeIds(CHUNK_SIZE));
                        logger.info("получение диапазона id комментариев книг из базы данных");
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