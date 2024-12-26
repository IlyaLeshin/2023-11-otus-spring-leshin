package ru.otus.hw.datamigration.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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
import ru.otus.hw.datamigration.models.MigrationAuthor;
import ru.otus.hw.datamigration.models.MigrationBook;
import ru.otus.hw.datamigration.models.MigrationComment;
import ru.otus.hw.datamigration.models.MigrationGenre;
import ru.otus.hw.datamigration.repositories.MigrationGenreRepository;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Конфигурация для миграции библиотеки должна")
@SpringBootTest
@SpringBatchTest
class JobConfigTest {
    public static final String JOB_NAME = "migrateLibraryDbJob";

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    MigrationGenreRepository migrationGenreRepository;

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

        long migrationAuthorCountBeforeJob = getMigrationTableElementsCount(MigrationAuthor.class);
        long migrationGenreCountBeforeJob = getMigrationTableElementsCount(MigrationGenre.class);
        long migrationBookCountBeforeJob = getMigrationTableElementsCount(MigrationBook.class);
        long migrationCommentCountBeforeJob = getMigrationTableElementsCount(MigrationComment.class);

        Job job = jobLauncherTestUtils.getJob();
        assertThat(job).isNotNull()
                .extracting(Job::getName)
                .isEqualTo(JOB_NAME);

        JobParameters parameters = new JobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(parameters);

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        long migrationAuthorCountAfterJob = getMigrationTableElementsCount(MigrationAuthor.class);
        long migrationGenreCountAfterJob = getMigrationTableElementsCount(MigrationGenre.class);
        long migrationBookCountAfterJob = getMigrationTableElementsCount(MigrationBook.class);
        long migrationCommentCountAfterJob = getMigrationTableElementsCount(MigrationComment.class);

        assertThat(migrationAuthorCountAfterJob-migrationAuthorCountBeforeJob).isEqualTo(originalAuthorsCount);
        assertThat(migrationGenreCountAfterJob-migrationGenreCountBeforeJob).isEqualTo(originalGenreCount);
        assertThat(migrationBookCountAfterJob-migrationBookCountBeforeJob).isEqualTo(originalBookCount);
        assertThat(migrationCommentCountAfterJob-migrationCommentCountBeforeJob).isEqualTo(originalCommentCount);
    }

    private Long getOriginalTableElementsCount(Class<?> className) {
        return mongoOperations.query(className).count();
    }

    private Long getMigrationTableElementsCount(Class<?> className) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<?> classRoot = criteriaQuery.from(className);
        criteriaQuery.select(builder.count(classRoot));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }
}