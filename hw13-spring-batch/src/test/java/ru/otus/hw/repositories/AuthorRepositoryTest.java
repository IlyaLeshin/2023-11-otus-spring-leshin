package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import ru.otus.hw.models.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе MongoDB для работы с авторами ")
@DataMongoTest
class AuthorRepositoryTest {

    private static final int EXPECTED_AUTHORS_COUNT = 3;

    private static final String FIRST_AUTHOR_ID = "a1";

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @DisplayName("должен загружать автора по id")
    @Test
    void shouldReturnCorrectAuthorById() {
        var expectedAuthor = mongoOperations.findById(FIRST_AUTHOR_ID, Author.class);
        var returnedAuthor = authorRepository.findById(FIRST_AUTHOR_ID);

        assertThat(returnedAuthor)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedAuthor);
    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorList() {

        var authors = authorRepository.findAll();

        assertThat(authors).isNotNull().hasSize(EXPECTED_AUTHORS_COUNT)
                .allMatch(author -> !author.getFullName().isEmpty());

    }
}