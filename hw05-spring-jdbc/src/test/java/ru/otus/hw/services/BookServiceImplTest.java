package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.when;

@DisplayName("Сервис для работы с книгами должен")
@SpringBootTest(classes = BookServiceImpl.class)
class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private BookConverter bookConverter;

    private List<BookDto> dbBookDtos;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        List<AuthorDto> dbAuthorDtos = getDbAuthorDtos();
        List<GenreDto> dbGenreDtos = getDbGenreDtos();
        dbBookDtos = getDbBookDtos(dbAuthorDtos, dbGenreDtos);

        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("загружать книгу по id. текущий метод: findById(long id)")
    @Test
    void findByIdTest() {
        Optional<BookDto> expectedBookDto = Optional.ofNullable(dbBookDtos.get(0));
        when(bookRepository.findById(1)).thenReturn(Optional.of(dbBooks.get(0)));
        when(bookConverter.modelToDto(dbBooks.get(0))).thenReturn(dbBookDtos.get(0));
        Optional<BookDto> actualBookDto = bookService.findById(1);

        assertThat(actualBookDto)
                .isEqualTo(expectedBookDto);
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
        BookDto expectedBookDto = dbBookDtos.get(0);
        String title = "Book_1";
        long authorId = 1L;
        Set<Long> genresIds = dbGenres.stream().map(Genre::getId).collect(Collectors.toSet());
        when(authorRepository.findById(1)).thenReturn(Optional.ofNullable(dbAuthors.get(0)));
        when(genreRepository.findAllByIds(genresIds)).thenReturn(dbGenres);
        when(bookRepository.save(new Book(0, title, dbAuthors.get(0), dbGenres))).thenReturn(dbBooks.get(0));
        when(bookConverter.modelToDto(dbBooks.get(0))).thenReturn(dbBookDtos.get(0));
        BookDto actualBookDto = bookService.insert(title, authorId, genresIds);

        assertThat(actualBookDto)
                .isEqualTo(expectedBookDto);
    }

    @DisplayName("Обновлять книгу в БД. текущий метод: update(long id, String title, long authorId, Set<Long> genresIds)")
    @Test
    void updateTest() {
        BookDto expectedBookDto = dbBookDtos.get(0);
        String title = "Book_1";
        long authorId = 1L;
        Set<Long> genresIds = dbGenres.stream().map(Genre::getId).collect(Collectors.toSet());
        when(authorRepository.findById(1)).thenReturn(Optional.ofNullable(dbAuthors.get(0)));
        when(genreRepository.findAllByIds(genresIds)).thenReturn(dbGenres);
        when(bookRepository.save(new Book(1L, title, dbAuthors.get(0), dbGenres))).thenReturn(dbBooks.get(0));
        when(bookConverter.modelToDto(dbBooks.get(0))).thenReturn(dbBookDtos.get(0));
        BookDto actualBookDto = bookService.update(1L, title, authorId, genresIds);

        assertThat(actualBookDto)
                .isEqualTo(expectedBookDto);
    }

    @DisplayName("отправлять запрос на удаление книги из БД. текущий метод: deleteById(long id)")
    @Test
    public void deleteById() {
        bookService.deleteById(1L);

        verify(bookRepository).deleteById(1L);
    }

    private static List<AuthorDto> getDbAuthorDtos() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto(id, "Author_" + id))
                .toList();
    }

    private static List<GenreDto> getDbGenreDtos() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new GenreDto(id, "Genre_" + id))
                .toList();
    }

    private static List<BookDto> getDbBookDtos(List<AuthorDto> dbAuthorDtos, List<GenreDto> dbGenreDtos) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookDto((long) id,
                        "BookTitle_" + id,
                        dbAuthorDtos.get(id - 1),
                        dbGenreDtos.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }
}
