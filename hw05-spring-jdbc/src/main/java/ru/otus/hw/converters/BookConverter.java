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

    public String dtoToString(BookDto book) {
        var genresString = book.getGenreDtoList().stream()
                .map(genreConverter::dtoToString)
                .map("{%s}"::formatted)
                .collect(Collectors.joining(", "));
        return "Id: %d, title: %s, author: {%s}, genres: [%s]".formatted(
                book.getId(),
                book.getTitle(),
                authorConverter.dtoToString(book.getAuthorDto()),
                genresString);
    }

    public Book dtoToModel(BookDto bookDto) {
        return new Book(bookDto.getId(),
                bookDto.getTitle(),
                authorConverter.dtoToModel(bookDto.getAuthorDto()),
                bookDto.getGenreDtoList().stream().map(genreConverter::dtoToModel).toList());
    }

    public BookDto modelToDto(Book book) {

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthorDto(authorConverter.modelToDto(book.getAuthor()));
        bookDto.setGenreDtoList(book.getGenres().stream()
                .map(genreConverter::modelToDto).toList());
        return bookDto;
    }
}
