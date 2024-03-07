package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с авторами ")
@DataJpaTest
@Import({JpaAuthorRepository.class})
class JpaAuthorRepositoryTest {

    private static final int EXPECTED_AUTHORS_COUNT = 3;

    private static final long FIRST_AUTHOR_ID = 1L;

    @Autowired
    private JpaAuthorRepository jpaAuthorRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("должен загружать книгу по id")
    @MethodSource("getDbAuthors")
    void shouldReturnCorrectAuthorById() {
        var expectedAuthor = testEntityManager.find(Author.class, FIRST_AUTHOR_ID);
        var optionalActualAuthor = jpaAuthorRepository.findById(FIRST_AUTHOR_ID);

        assertThat(optionalActualAuthor)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedAuthor);
    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorList() {

        var authors = jpaAuthorRepository.findAll();

        assertThat(authors).isNotNull().hasSize(EXPECTED_AUTHORS_COUNT)
                .allMatch(author -> !author.getFullName().isEmpty())
                .containsOnlyOnce(testEntityManager.find(Author.class, FIRST_AUTHOR_ID));
    }
}