package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.BookWithCommentsConverter;
import ru.otus.hw.dto.*;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.when;

@DisplayName("Сервис для работы с книгами должен")
@SpringBootTest(classes = BookServiceImpl.class)
class BookServiceImplTest {
    private static final String FIRST_BOOK_ID = "b1";

    private static final String FIRST_AUTHOR_ID = "a1";

    private static final String FIRST_GENRE_ID = "g1";

    private static final String SECOND_GENRE_ID = "g2";

    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private BookConverter bookConverter;

    @MockBean
    private BookWithCommentsConverter bookWithCommentsConverter;

    private List<BookDto> dbBookDtos;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        List<AuthorDto> dbAuthorDtos = getDbAuthorDtos();
        List<GenreDto> dbGenreDtos = getDbGenreDtos();
        dbBookDtos = getDbBookDtos(dbAuthorDtos, dbGenreDtos);

        List<Author> dbAuthors = getDbAuthors();
        List<Genre> dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("загружать книгу по id. текущий метод: findById(long id)")
    @Test
    void findByIdTest() {
        BookDto expectedBookDto = dbBookDtos.get(0);
        when(bookRepository.findById(FIRST_BOOK_ID)).thenReturn(Optional.of(dbBooks.get(0)));
        when(bookConverter.modelToDto(dbBooks.get(0))).thenReturn(dbBookDtos.get(0));
        BookDto actualBookDto = bookService.findById(FIRST_BOOK_ID);

        assertThat(actualBookDto)
                .isEqualTo(expectedBookDto);
    }

    @DisplayName("загружать книгу с комментариями по id. текущий метод: findWithCommentsById(long id)")
    @Test
    void findWithCommentsByIdTest() {
        Book bookWithComments = new Book(FIRST_BOOK_ID, "BookTitle_1", dbBooks.get(0).getAuthor(),
                dbBooks.get(0).getGenres(), List.of());
        BookWithCommentsDto bookWithCommentsDto = new BookWithCommentsDto(FIRST_BOOK_ID, "BookTitle_1", dbBookDtos.get(0).getAuthorDto(),
                dbBookDtos.get(0).getGenreDtoList(), List.of());

        when(bookRepository.findById(FIRST_BOOK_ID)).thenReturn(Optional.of(bookWithComments));
        when(bookWithCommentsConverter.modelToDto(bookWithComments)).thenReturn(bookWithCommentsDto);
        BookWithCommentsDto actualBookDto = bookService.findWithCommentsById(FIRST_BOOK_ID);

        assertThat(actualBookDto)
                .isEqualTo(bookWithCommentsDto);
    }

    @DisplayName("загружать список всех книг. текущий метод: findAll()")
    @Test
    void findAllTest() {
        List<BookDto> expectedBookDtos = dbBookDtos;
        when(bookRepository.findAll()).thenReturn(dbBooks);
        for (int i = 0; i < dbBooks.size(); i++) {
            when(bookConverter.modelToDto(dbBooks.get(i))).thenReturn(dbBookDtos.get(i));
        }
        List<BookDto> actualBookDtos = bookService.findAll();

        assertEquals(expectedBookDtos, actualBookDtos);

    }

    @DisplayName("Сохранять книгу в БД. текущий метод: insert(String title, long authorId, Set<Long> genresIds)")
    @Test
    void insertTest() {
        var newBook = new Book(null, "saved_Book", dbBooks.get(0).getAuthor(),
                dbBooks.get(0).getGenres());
        var newBookUpdateDto = new BookCreateDto("saved_Book", FIRST_AUTHOR_ID, Set.of(FIRST_GENRE_ID, SECOND_GENRE_ID));
        var expectedBookDto = new BookDto(FIRST_BOOK_ID, "saved_Book", dbBookDtos.get(0).getAuthorDto(),
                dbBookDtos.get(0).getGenreDtoList());

        when(bookConverter.modelToDto(bookRepository.save(newBook))).thenReturn(expectedBookDto);
        when(authorRepository.findById(FIRST_AUTHOR_ID)).thenReturn(Optional.ofNullable(dbBooks.get(0).getAuthor()));
        when(genreRepository.findAllByIdIn(Set.of(FIRST_GENRE_ID, SECOND_GENRE_ID))).thenReturn(dbBooks.get(0).getGenres());
        var returnedBookDto = bookService.insert(newBookUpdateDto);

        verify(bookRepository).save(newBook);
        assertThat(returnedBookDto).isEqualTo(expectedBookDto);
    }

    @DisplayName("Обновлять книгу в БД. текущий метод: update(long id, String title, long authorId, Set<Long> genresIds)")
    @Test
    void updateTest() {
        var bookBeforeUpdate = dbBooks.get(0);
        var bookToUpdate = new Book(FIRST_BOOK_ID, "updated_Book", dbBooks.get(0).getAuthor(),
                dbBooks.get(0).getGenres());
        var bookUpdateDto = new BookUpdateDto(FIRST_BOOK_ID, "updated_Book", FIRST_AUTHOR_ID, Set.of(FIRST_GENRE_ID, SECOND_GENRE_ID));
        var expectedBookDto = new BookDto(FIRST_BOOK_ID, "updated_Book", dbBookDtos.get(0).getAuthorDto(),
                dbBookDtos.get(0).getGenreDtoList());

        when(bookRepository.findById(bookToUpdate.getId())).thenReturn(Optional.of(bookToUpdate));
        when(bookRepository.findById(FIRST_BOOK_ID)).thenReturn(Optional.ofNullable(bookBeforeUpdate));
        when(bookConverter.modelToDto(Optional.ofNullable(bookBeforeUpdate).get())).thenReturn(dbBookDtos.get(0));
        when(authorRepository.findById(FIRST_AUTHOR_ID)).thenReturn(Optional.ofNullable(dbBooks.get(0).getAuthor()));
        when(genreRepository.findAllByIdIn(Set.of(FIRST_GENRE_ID, SECOND_GENRE_ID))).thenReturn(dbBooks.get(0).getGenres());
        when(bookConverter.modelToDto(bookRepository.save(bookToUpdate))).thenReturn(expectedBookDto);

        var returnedBookDto = bookService.update(bookUpdateDto);

        verify(bookRepository).save(bookToUpdate);
        verify(commentRepository).findAllByBookId(FIRST_BOOK_ID);
        assertThat(returnedBookDto).isEqualTo(expectedBookDto);
    }

    @DisplayName("отправлять запрос на удаление книги из БД. текущий метод: deleteById(long id)")
    @Test
    public void deleteByIdTest() {
        bookService.deleteById(FIRST_BOOK_ID);

        verify(bookRepository).deleteById(FIRST_BOOK_ID);
        verify(commentRepository).deleteAllByBookId(FIRST_BOOK_ID);
    }

    private static List<AuthorDto> getDbAuthorDtos() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto("a" + id, "Author_" + id))
                .toList();
    }

    private static List<GenreDto> getDbGenreDtos() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new GenreDto("g" + id, "Genre_" + id))
                .toList();
    }

    private static List<BookDto> getDbBookDtos(List<AuthorDto> dbAuthorDtos, List<GenreDto> dbGenreDtos) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookDto("b" + id,
                        "BookTitle_" + id,
                        dbAuthorDtos.get(id - 1),
                        dbGenreDtos.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author("a" + id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre("g" + id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book("b" + id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }
}
