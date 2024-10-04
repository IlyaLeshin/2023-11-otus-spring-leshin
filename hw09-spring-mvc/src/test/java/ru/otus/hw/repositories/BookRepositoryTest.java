package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе MongoDB для работы с книгами ")
@DataMongoTest
class BookRepositoryTest {

    private static final int EXPECTED_NUMBER_OF_BOOKS = 3;

    private static final String FIRST_BOOK_ID = "b1";

    private static final String SECOND_BOOK_ID = "b2";

    private static final String FIRST_AUTHOR_ID = "a1";

    private static final String FIRST_GENRE_ID = "g1";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MongoOperations mongoOperations;


    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        var optionalActualBook = bookRepository.findById(FIRST_BOOK_ID);
        var expectedBook = mongoOperations.findById(FIRST_BOOK_ID, Book.class);

        assertThat(optionalActualBook).isPresent().get().usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать книгу c комментариями по id")
    @Test
    void shouldReturnCorrectBookWithCommentsById() {
        var optionalActualBook = bookRepository.findById(FIRST_BOOK_ID);
        var expectedBook = mongoOperations.findById(FIRST_BOOK_ID, Book.class);

        assertThat(optionalActualBook).isPresent().get().usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = bookRepository.findAll();
        var expectedBook = mongoOperations.findAll(Book.class);
        assertThat(actualBooks).usingRecursiveComparison().isEqualTo(expectedBook);
        assertThat(actualBooks).isNotNull().hasSize(EXPECTED_NUMBER_OF_BOOKS)
                .allMatch(book -> !book.getTitle().isEmpty())
                .allMatch(book -> !book.getGenres().isEmpty())
                .allMatch(book -> !book.getAuthor().getFullName().isEmpty());
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var author = mongoOperations.findById(FIRST_AUTHOR_ID, Author.class);
        var genre = mongoOperations.findById(FIRST_GENRE_ID, Genre.class);
        var expectedBook = new Book(null, "saved_Book", author, List.of(genre));
        var returnedBook = bookRepository.save(expectedBook);

        assertThat(returnedBook).isNotNull()
                .matches(book -> !book.getId().isEmpty());

        assertThat(bookRepository.findById(returnedBook.getId()))
                .isPresent().get().usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен сохранять книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var newBook = new Book();
        newBook.setTitle("BookTitle_4");
        var testGenre = mongoOperations.findById(FIRST_GENRE_ID, Genre.class);
        newBook.setGenres(List.of(testGenre));
        var testAuthor = mongoOperations.findById(FIRST_AUTHOR_ID, Author.class);
        newBook.setAuthor(testAuthor);

        var expectedBook = bookRepository.save(newBook);

        assertThat(expectedBook).isNotNull();
        assertThat(expectedBook.getId()).isNotNull();

        var actualBook = mongoOperations.findById(expectedBook.getId(), Book.class);

        assertThat(actualBook)
                .isNotNull().usingRecursiveComparison()
                .isEqualTo(expectedBook);

        assertThat(actualBook)
                .matches(book -> Objects.equals(book.getId(), newBook.getId()))
                .matches(book -> book.getTitle().equals("BookTitle_4"))
                .matches(book -> !book.getGenres().isEmpty() && Objects.equals(book.getGenres().get(0).getId(), FIRST_GENRE_ID))
                .matches(book -> book.getAuthor() != null && Objects.equals(book.getAuthor().getId(), FIRST_AUTHOR_ID));
    }

    @DisplayName("должен удалять книгу по id и каскадно удалять комментарии")
    @Test
    void shouldDeleteBook() {
        var book = bookRepository.findById(SECOND_BOOK_ID);

        assertThat(book).isPresent();

        bookRepository.deleteById(book.get().getId());

        assertThat(bookRepository.findById(SECOND_BOOK_ID)).isEmpty();
    }
}
