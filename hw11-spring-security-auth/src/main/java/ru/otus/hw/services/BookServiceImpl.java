package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.BookWithCommentsConverter;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.BookWithCommentsDto;
import ru.otus.hw.exceptions.AuthorNotFoundException;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.exceptions.GenreNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookConverter bookConverter;

    private final BookWithCommentsConverter bookWithCommentsConverter;

    @Override
    @Transactional(readOnly = true)
    public BookDto findById(long id) {
        return bookRepository.findById(id).map(bookConverter::modelToDto).orElseThrow(() ->
                new BookNotFoundException("Book with id %s not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public BookWithCommentsDto findWithCommentsById(long id) {
        return bookRepository.findById(id).map(bookWithCommentsConverter::modelToDto).orElseThrow(() ->
                new BookNotFoundException("Book with id %s not found".formatted(id)));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream().map(bookConverter::modelToDto).toList();
    }

    @Override
    @Transactional
    public BookDto insert(BookCreateDto bookCreateDto) {
        String title = bookCreateDto.getTitle();
        long authorId = bookCreateDto.getAuthorId();
        Set<Long> genresIds = bookCreateDto.getGenreIds();
        return save(0L, title, authorId, genresIds);

    }

    @Override
    @Transactional
    public BookDto update(BookUpdateDto bookUpdateDto) {
        long id = bookUpdateDto.getId();
        if (findById(id) != null) {
            String title = bookUpdateDto.getTitle();
            long authorId = bookUpdateDto.getAuthorId();
            Set<Long> genresIds = bookUpdateDto.getGenreIds();

            return save(id, title, authorId, genresIds);
        }
        throw new BookNotFoundException("Book with id %s not found".formatted(id));
    }

    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private BookDto save(long id, String title, long authorId, Set<Long> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AuthorNotFoundException("Author with id %d not found".formatted(authorId)));
        var genres = genreRepository.findAllByIdIn(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new GenreNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        var book = new Book(id, title, author, genres);
        return bookConverter.modelToDto(bookRepository.save(book));
    }
}
