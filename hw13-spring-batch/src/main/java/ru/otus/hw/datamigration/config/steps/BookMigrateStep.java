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
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.datamigration.dto.BookMigrationDto;
import ru.otus.hw.models.Book;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class BookMigrateStep {
    public static final String ITEM_READER_NAME = "bookMongoReader";

    public static final String STEP_NAME = "bookMigrateStep";

    public static final String CREATE_TEMP_TABLE_NAME = "createBooksTempTable";

    public static final String DROP_TEMP_TABLE_NAME = "dropBooksTempTable";

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
    public TaskletStep createBooksTempTable(DataSource dataSource,
                                            PlatformTransactionManager platformTransactionManager,
                                            JobRepository jobRepository
    ) {
        return new StepBuilder(CREATE_TEMP_TABLE_NAME, jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute("""
                            create table if not exists temp_mapping_books_ids (
                            id_original varchar(255) not null unique,
                            id_migration bigserial not null unique
                            )
                            """);
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

    @Bean
    public CompositeItemWriter<BookMigrationDto> compositeBookItemWriter(
            JdbcBatchItemWriter<BookMigrationDto> booksIdsWriter,
            JdbcBatchItemWriter<BookMigrationDto> booksWriter,
            ItemWriter<BookMigrationDto> booksGenresRelationWriter) {
        return new CompositeItemWriter<>(List.of(booksIdsWriter, booksWriter, booksGenresRelationWriter));
    }

    @Bean
    public JdbcBatchItemWriter<BookMigrationDto> booksIdsWriter(DataSource dataSource) {
        JdbcBatchItemWriter<BookMigrationDto> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("""
                insert into temp_mapping_books_ids (id_original, id_migration)
                values (:id, select next value for book_seq)
                """);
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public JdbcBatchItemWriter<BookMigrationDto> booksWriter(DataSource dataSource) {
        JdbcBatchItemWriter<BookMigrationDto> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("""
                insert into books (id, title, author_id)
                values (
                (select id_migration from temp_mapping_books_ids where id_original = :id),
                :title,
                (select id_migration from temp_mapping_authors_ids where id_original = :authorId))
                """);
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public ItemWriter<BookMigrationDto> booksGenresRelationWriter(
            DataSource dataSource,
            NamedParameterJdbcOperations namedParameterJdbcTemplate) {
        return new RelationBookGenreItemWriter(namedParameterJdbcTemplate);
    }

    @Bean
    public TaskletStep dropBooksTempTable(DataSource dataSource,
                                          PlatformTransactionManager platformTransactionManager,
                                          JobRepository jobRepository
    ) {
        return new StepBuilder(DROP_TEMP_TABLE_NAME, jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute("drop table temp_mapping_books_ids");
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }


    @Bean
    public Step migrateBookStep(ItemReader<Book> bookMongoItemReader,
                                CompositeItemWriter<BookMigrationDto> compositeBookItemWriter,
                                ItemProcessor<Book, BookMigrationDto> processor,
                                PlatformTransactionManager platformTransactionManager,
                                JobRepository jobRepository
    ) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<Book, BookMigrationDto>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(bookMongoItemReader)
                .processor(processor)
                .writer(compositeBookItemWriter)
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

    public static class RelationBookGenreItemWriter implements ItemWriter<BookMigrationDto> {

        private final NamedParameterJdbcOperations namedParameterJdbcOperations;

        public RelationBookGenreItemWriter(NamedParameterJdbcOperations namedParameterJdbcOperations) {
            this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        }

        @Override
        public void write(Chunk<? extends BookMigrationDto> chunk) throws Exception {

            for (BookMigrationDto book : chunk.getItems()) {
                SqlParameterSource[] batchArgs = SqlParameterSourceUtils.createBatch(
                        book.getGenreIds().stream().map(genreId ->
                                new BookGenreRelation(book.getId(), genreId)).toList());
                namedParameterJdbcOperations
                        .batchUpdate("""
                                insert into books_genres(book_id, genre_id)
                                values (
                                select id_migration from temp_mapping_books_ids where id_original = :bookId,
                                select id_migration from temp_mapping_genres_ids where id_original = :genreId
                                )
                                """, batchArgs);
            }
        }

        private record BookGenreRelation(String bookId, String genreId) {
        }
    }
}