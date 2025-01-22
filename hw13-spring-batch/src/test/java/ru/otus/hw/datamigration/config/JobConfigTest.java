package ru.otus.hw.datamigration.config;

import jakarta.persistence.EntityManager;
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

    @BeforeEach
    void clearMetaData() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @DisplayName("успешно выполнятся")
    @Test
    void testJob() throws Exception {
//        long originalAuthorsCount = getOriginalTableElementsCount(Author.class);
//        long originalGenreCount = getOriginalTableElementsCount(Genre.class);
//        long originalBookCount = getOriginalTableElementsCount(Book.class);
//        long originalCommentCount = getOriginalTableElementsCount(Comment.class);
//
//        long migrationAuthorCountBeforeJob = getMigrationTableElementsCount(AuthorMigrationDto.class);
//        long migrationGenreCountBeforeJob = getMigrationTableElementsCount(GenreMigrationDto.class);
//        long migrationBookCountBeforeJob = getMigrationTableElementsCount(BookMigrationDto.class);
//        long migrationCommentCountBeforeJob = getMigrationTableElementsCount(CommentMigrationDto.class);

        Job job = jobLauncherTestUtils.getJob();
        assertThat(job).isNotNull()
                .extracting(Job::getName)
                .isEqualTo(JOB_NAME);

        JobParameters parameters = new JobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(parameters);

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
//
//        long migrationAuthorCountAfterJob = getMigrationTableElementsCount(AuthorMigrationDto.class);
//        long migrationGenreCountAfterJob = getMigrationTableElementsCount(GenreMigrationDto.class);
//        long migrationBookCountAfterJob = getMigrationTableElementsCount(BookMigrationDto.class);
//        long migrationCommentCountAfterJob = getMigrationTableElementsCount(CommentMigrationDto.class);
//
//        assertThat(migrationAuthorCountAfterJob-migrationAuthorCountBeforeJob).isEqualTo(originalAuthorsCount);
//        assertThat(migrationGenreCountAfterJob-migrationGenreCountBeforeJob).isEqualTo(originalGenreCount);
//        assertThat(migrationBookCountAfterJob-migrationBookCountBeforeJob).isEqualTo(originalBookCount);
//        assertThat(migrationCommentCountAfterJob-migrationCommentCountBeforeJob).isEqualTo(originalCommentCount);
    }

//    private Long getOriginalTableElementsCount(Class<?> className) {
//        return mongoOperations.query(className).count();
//    }
//
//    private Long getMigrationTableElementsCount(Class<?> className) {
//        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
//        Root<?> classRoot = criteriaQuery.from(className);
//        criteriaQuery.select(builder.count(classRoot));
//        return entityManager.createQuery(criteriaQuery).getSingleResult();
//    }
}