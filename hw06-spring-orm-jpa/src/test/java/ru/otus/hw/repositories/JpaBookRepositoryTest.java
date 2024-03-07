package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с книгами ")
@DataJpaTest
@Import({JpaBookRepository.class})
class JpaBookRepositoryTest {

    private static final int EXPECTED_NUMBER_OF_BOOKS = 3;
    private static final long FIRST_BOOK_ID = 1L;
    private static final long FIRST_AUTHOR_ID = 1L;
    private static final long FIRST_GENRE_ID = 1L;

    @Autowired
    private JpaBookRepository jpaBookRepository;

    @Autowired
    private TestEntityManager testEntityManager;


    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        var optionalActualBook = jpaBookRepository.findById(FIRST_BOOK_ID);
        var expectedBook = testEntityManager.find(Book.class, FIRST_BOOK_ID);

        assertThat(optionalActualBook).isPresent().get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать книгу c комментариями по id")
    @Test
    void shouldReturnCorrectBookWithCommentsById() {
        var optionalActualBook = jpaBookRepository.findWithCommentsById(FIRST_BOOK_ID);
        var expectedBook = testEntityManager.find(Book.class, FIRST_BOOK_ID);

        assertThat(optionalActualBook).isPresent().get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = jpaBookRepository.findAll();

        assertThat(actualBooks).isNotNull().hasSize(EXPECTED_NUMBER_OF_BOOKS)
                .allMatch(book -> !book.getTitle().isEmpty())
                .allMatch(book -> !book.getGenres().isEmpty())
                .allMatch(book -> !book.getAuthor().getFullName().isEmpty());
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var author = testEntityManager.find(Author.class, FIRST_AUTHOR_ID);
        var genre = testEntityManager.find(Genre.class, FIRST_GENRE_ID);
        var expectedBook = new Book(0, "saved_Book", author, List.of(genre));
        var returnedBook = jpaBookRepository.save(expectedBook);

        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0);

        assertThat(jpaBookRepository.findById(returnedBook.getId()))
                .isPresent().get().isEqualTo(returnedBook);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("должен сохранять книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var newBook = new Book();
        newBook.setTitle("BookTitle_4");
        var testGenre = testEntityManager.find(Genre.class, FIRST_GENRE_ID);
        newBook.setGenres(List.of(testGenre));
        var testAuthor = testEntityManager.find(Author.class, FIRST_AUTHOR_ID);
        newBook.setAuthor(testAuthor);

        var expectedBook = jpaBookRepository.save(newBook);

        assertThat(expectedBook).isNotNull();
        assertThat(expectedBook.getId()).isGreaterThan(0);

        var actualBook = testEntityManager.find(Book.class, expectedBook.getId());

        assertThat(actualBook)
                .isNotNull()
                .isEqualTo(expectedBook);

        assertThat(actualBook)
                .matches(book -> book.getId() == newBook.getId())
                .matches(book -> book.getTitle().equals("BookTitle_4"))
                .matches(book -> !book.getGenres().isEmpty() && book.getGenres().get(0).getId() == FIRST_GENRE_ID)
                .matches(book -> book.getAuthor() != null && book.getAuthor().getId() == FIRST_AUTHOR_ID);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        var book = jpaBookRepository.findById(FIRST_BOOK_ID);

        assertThat(book).isPresent();

        jpaBookRepository.deleteById(book.get().getId());

        assertThat(jpaBookRepository.findById(FIRST_BOOK_ID)).isEmpty();
    }
}
