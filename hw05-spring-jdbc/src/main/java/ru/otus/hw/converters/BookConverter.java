package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    public String bookToString(BookDto book) {
        var genresString = book.getGenreDtoList().stream()
                .map(genreConverter::genreToString)
                .map("{%s}"::formatted)
                .collect(Collectors.joining(", "));
        return "Id: %d, title: %s, author: {%s}, genres: [%s]".formatted(
                book.getId(),
                book.getTitle(),
                authorConverter.authorToString(book.getAuthorDto()),
                genresString);
    }

    public Book bookDtoToBook(BookDto bookDto) {
        return new Book(bookDto.getId(),
                bookDto.getTitle(),
                authorConverter.authorDtoToAuthor(bookDto.getAuthorDto()),
                bookDto.getGenreDtoList().stream().map(genreConverter::genreDtotoGenre).toList());
    }

    public BookDto bookToBookDto(Book book) {

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthorDto(authorConverter.authorToAuthorDto(book.getAuthor()));
        bookDto.setGenreDtoList(book.getGenres().stream()
                .map(genreConverter::genreToGenreDto).collect(Collectors.toSet()));
        return bookDto;
    }
}
