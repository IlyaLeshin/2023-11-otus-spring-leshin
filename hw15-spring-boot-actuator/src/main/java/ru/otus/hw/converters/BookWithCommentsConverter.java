package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookWithCommentsDto;
import ru.otus.hw.models.Book;

@RequiredArgsConstructor
@Component
public class BookWithCommentsConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    private final CommentConverter commentConverter;

    public BookWithCommentsDto modelToDto(Book book) {

        BookWithCommentsDto bookDto = new BookWithCommentsDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthorDto(authorConverter.modelToDto(book.getAuthor()));
        bookDto.setGenreDtoList(book.getGenres().stream()
                .map(genreConverter::modelToDto).toList());
        bookDto.setCommentDtoList(book.getComments().stream()
                .map(commentConverter::modelToDto).toList());
        return bookDto;
    }
}
