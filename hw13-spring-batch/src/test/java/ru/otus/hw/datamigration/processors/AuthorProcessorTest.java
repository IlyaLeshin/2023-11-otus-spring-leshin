package ru.otus.hw.datamigration.processors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.datamigration.dto.AuthorMigrationDto;
import ru.otus.hw.models.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Процессор для преобразования с авторов должен")
@SpringBootTest(classes = {AuthorProcessor.class})
class AuthorProcessorTest {

    @Autowired
    AuthorProcessor authorProcessor;

    @DisplayName("преобразовывать Author в MigrationAuthor. текущий метод: process()")
    @Test
    void processTest() throws Exception {
        Author authorOne = new Author("ObjectId_1", "Author_1");
        Author authorTwo = new Author("ObjectId_2", "Author_2");

        AuthorMigrationDto migrationAuthorOne = authorProcessor.process(authorOne);

        assertThat(migrationAuthorOne).isNotNull();
        assertThat(migrationAuthorOne.getFullName()).isEqualTo(authorOne.getFullName());
        assertThat(migrationAuthorOne.getId()).isEqualTo("ObjectId_1");

        AuthorMigrationDto migrationAuthorTwo = authorProcessor.process(authorTwo);

        assertThat(migrationAuthorTwo).isNotNull();
        assertThat(migrationAuthorTwo.getFullName()).isEqualTo(authorTwo.getFullName());
        assertThat(migrationAuthorTwo.getId()).isEqualTo("ObjectId_2");
    }
}