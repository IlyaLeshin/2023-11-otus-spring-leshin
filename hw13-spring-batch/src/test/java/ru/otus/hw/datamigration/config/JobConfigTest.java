package ru.otus.hw.datamigration.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

@DisplayName("Конфигурация для миграции библиотеки должна")
@SpringBootTest
@SpringBatchTest
class JobConfigTest {

    public static final String JOB_NAME = "migrateLibraryDbJob";

    public static final String AUTHORS_TABLE_NAME = "authors";
    public static final String GENRES_TABLE_NAME = "genres";
    public static final String BOOKS_TABLE_NAME = "books";
    public static final String COMMENTS_TABLE_NAME = "comments";
    public static final String BOOKS_GENRES_RELATION_TABLE_NAME = "books_genres";
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    private MongoOperations mongoOperations;

    @BeforeEach
    void clearMetaData() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @DisplayName("успешно выполнятся")
    @Test
    void testJob() throws Exception {
        long originalAuthorsCount = getOriginalTableElementsCount(Author.class);
        long originalGenreCount = getOriginalTableElementsCount(Genre.class);
        long originalBookCount = getOriginalTableElementsCount(Book.class);
        long originalCommentCount = getOriginalTableElementsCount(Comment.class);

        long migrationAuthorCountBeforeJob = getMigrationTableElementsCount(AUTHORS_TABLE_NAME);
        long migrationGenreCountBeforeJob = getMigrationTableElementsCount(GENRES_TABLE_NAME);
        long migrationBookCountBeforeJob = getMigrationTableElementsCount(BOOKS_TABLE_NAME);
        long migrationCommentCountBeforeJob = getMigrationTableElementsCount(COMMENTS_TABLE_NAME);
        long migrationBooksGenresRelationCountBeforeJob = getMigrationTableElementsCount(BOOKS_GENRES_RELATION_TABLE_NAME);

        Job job = jobLauncherTestUtils.getJob();
        assertThat(job).isNotNull()
                .extracting(Job::getName)
                .isEqualTo(JOB_NAME);

        JobParameters parameters = new JobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(parameters);

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        long migrationAuthorCountAfterJob = getMigrationTableElementsCount(AUTHORS_TABLE_NAME) - migrationAuthorCountBeforeJob;
        long migrationGenreCountAfterJob = getMigrationTableElementsCount(GENRES_TABLE_NAME) - migrationGenreCountBeforeJob;
        long migrationBookCountAfterJob = getMigrationTableElementsCount(BOOKS_TABLE_NAME) - migrationBookCountBeforeJob;
        long migrationCommentCountAfterJob = getMigrationTableElementsCount(COMMENTS_TABLE_NAME) - migrationCommentCountBeforeJob;
        long migrationBooksGenresRelationCountAfterJob = getMigrationTableElementsCount(BOOKS_GENRES_RELATION_TABLE_NAME) - migrationBooksGenresRelationCountBeforeJob;

        assertThat(migrationAuthorCountAfterJob - migrationAuthorCountBeforeJob).isEqualTo(originalAuthorsCount);
        assertThat(migrationGenreCountAfterJob - migrationGenreCountBeforeJob).isEqualTo(originalGenreCount);
        assertThat(migrationBookCountAfterJob - migrationBookCountBeforeJob).isEqualTo(originalBookCount);
        assertThat(migrationCommentCountAfterJob - migrationCommentCountBeforeJob).isEqualTo(originalCommentCount);

        assertThat(migrationBooksGenresRelationCountAfterJob).isGreaterThan(0);

    }

    private Long getOriginalTableElementsCount(Class<?> className) {
        return mongoOperations.query(className).count();
    }

    private int getMigrationTableElementsCount(String tableName) {
        return JdbcTestUtils.countRowsInTable(this.jdbcTemplate, tableName);
    }
}