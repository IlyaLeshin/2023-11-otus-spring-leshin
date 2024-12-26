package ru.otus.hw.datamigration.processors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.datamigration.cache.MigrationAuthorCache;
import ru.otus.hw.datamigration.models.MigrationAuthor;
import ru.otus.hw.datamigration.repositories.MigrationAuthorRepository;
import ru.otus.hw.models.Author;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Процессор для преобразования с авторов должен")
@SpringBootTest(classes = {AuthorProcessor.class})
class AuthorProcessorTest {

    @Autowired
    AuthorProcessor authorProcessor;

    @MockBean
    MigrationAuthorRepository migrationAuthorRepository;

    @MockBean
    MigrationAuthorCache migrationAuthorCache;

    @DisplayName("преобразовывать Author в MigrationAuthor. текущий метод: process()")
    @Test
    void processTest() throws Exception {
        Author authorOne = new Author("ObjectId_1", "Author_1");
        Author authorTwo = new Author("ObjectId_2", "Author_2");

        when(migrationAuthorRepository.getNextSequenceId()).thenReturn(1L);
        MigrationAuthor migrationAuthorOne = authorProcessor.process(authorOne);

        assertThat(migrationAuthorOne).isNotNull();
        assertThat(migrationAuthorOne.getFullName()).isEqualTo(authorOne.getFullName());
        assertThat(migrationAuthorOne.getId()).isEqualTo(1L);

        when(migrationAuthorRepository.getNextSequenceId()).thenReturn(2L);
        MigrationAuthor migrationAuthorTwo = authorProcessor.process(authorTwo);

        assertThat(migrationAuthorTwo).isNotNull();
        assertThat(migrationAuthorTwo.getFullName()).isEqualTo(authorTwo.getFullName());
        assertThat(migrationAuthorTwo.getId()).isEqualTo(2L);
    }
}