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
import ru.otus.hw.datamigration.dto.CommentMigrationDto;
import ru.otus.hw.models.Comment;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CommentMigrateStep {
    public static final String ITEM_READER_NAME = "commentMongoReader";

    public static final String STEP_NAME = "commentMigrateStep";

    public static final String CREATE_TEMP_TABLE_NAME = "createCommentsTempTable";

    public static final String DROP_TEMP_TABLE_NAME = "dropCommentsTempTable";

    private static final int CHUNK_SIZE = 20;

    private final Logger logger = LoggerFactory.getLogger("CommentMigrateStep");

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
    public TaskletStep createCommentsTempTable(DataSource dataSource,
                                              PlatformTransactionManager platformTransactionManager,
                                              JobRepository jobRepository
    ) {
        return new StepBuilder(CREATE_TEMP_TABLE_NAME, jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute("""
                            create table if not exists temp_mapping_comments_ids (
                            id_original varchar(255) not null unique,
                            id_migration bigserial not null unique
                            )
                            """);
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

    @Bean
    public CompositeItemWriter<CommentMigrationDto> compositeCommentItemWriter(
            JdbcBatchItemWriter<CommentMigrationDto> commentsIdsWriter,
            JdbcBatchItemWriter<CommentMigrationDto> commentsWriter) {
        return new CompositeItemWriter<>(List.of(commentsIdsWriter, commentsWriter));
    }

    @Bean
    public JdbcBatchItemWriter<CommentMigrationDto> commentsIdsWriter(DataSource dataSource) {
        JdbcBatchItemWriter<CommentMigrationDto> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("""
                insert into temp_mapping_comments_ids (id_original, id_migration)
                values (:id, select next value for comment_seq)
                """);
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public JdbcBatchItemWriter<CommentMigrationDto> commentsWriter(DataSource dataSource) {
        JdbcBatchItemWriter<CommentMigrationDto> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("""
                insert into comments (id, text, book_id)
                values (
                select id_migration from temp_mapping_comments_ids where id_original = :id,
                :text,
                select id_migration from temp_mapping_books_ids where id_original = :bookId);
                """);
        writer.setDataSource(dataSource);
        return writer;
    }


    @Bean
    public TaskletStep dropCommentsTempTable(DataSource dataSource,
                                            PlatformTransactionManager platformTransactionManager,
                                            JobRepository jobRepository
    ) {
        return new StepBuilder(DROP_TEMP_TABLE_NAME, jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute("drop table temp_mapping_comments_ids");
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

    @Bean
    public Step migrateCommentStep(ItemReader<Comment> commentMongoItemReader,
                                   CompositeItemWriter<CommentMigrationDto> commentWriter,
                                   ItemProcessor<Comment, CommentMigrationDto> processor,
                                   PlatformTransactionManager platformTransactionManager,
                                   JobRepository jobRepository
    ) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<Comment, CommentMigrationDto>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(commentMongoItemReader)
                .processor(processor)
                .writer(commentWriter)
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