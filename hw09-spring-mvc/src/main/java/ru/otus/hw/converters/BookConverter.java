package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    public BookUpdateDto dtoToUpdateDto(BookDto bookDto) {
        return new BookUpdateDto(
                bookDto.getId(),
                bookDto.getTitle(),
                bookDto.getAuthorDto().getId(),
                bookDto.getGenreDtoList().stream().map(GenreDto::getId).collect(Collectors.toSet())
        );
    }

    public BookCreateDto dtoToCreateDto(BookDto bookDto) {
        return new BookCreateDto(
                bookDto.getTitle(),
                bookDto.getAuthorDto().getId(),
                bookDto.getGenreDtoList().stream().map(GenreDto::getId).collect(Collectors.toSet())
        );
    }

    public BookDto updateDtoToDto(BookUpdateDto bookUpdateDto, Author author, List<Genre> genreList) {
        return new BookDto(
                bookUpdateDto.getId(),
                bookUpdateDto.getTitle(),
                authorConverter.modelToDto(author),
                genreList.stream().map(genreConverter::modelToDto).toList()
        );
    }

    public BookDto createDtoToDto(BookCreateDto bookCreateDto, Author author, List<Genre> genreList) {
        return new BookDto(
                null,
                bookCreateDto.getTitle(),
                authorConverter.modelToDto(author),
                genreList.stream().map(genreConverter::modelToDto).toList()
        );
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

    public Book dtoToModel(BookDto bookDto) {
        AuthorDto authorDto = bookDto.getAuthorDto();
        List<GenreDto> genreDtos = bookDto.getGenreDtoList();
        return new Book(bookDto.getId(), bookDto.getTitle(), authorConverter.dtoToModel(authorDto),
                genreDtos.stream().map(genreConverter::dtoToModel).toList());
    }
}
