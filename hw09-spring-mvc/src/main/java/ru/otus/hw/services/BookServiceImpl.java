package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
import ru.otus.hw.repositories.CommentRepository;
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

    private final CommentRepository commentRepository;

    private final BookConverter bookConverter;

    private final BookWithCommentsConverter bookWithCommentsConverter;

    @Override
    public BookDto findById(String id) {
        return bookRepository.findById(id).map(bookConverter::modelToDto).orElseThrow(() ->
                new BookNotFoundException("Book with id %s not found".formatted(id)));
    }

    @Override
    public BookWithCommentsDto findWithCommentsById(String id) {
        return bookRepository.findById(id).map(bookWithCommentsConverter::modelToDto).orElseThrow(() ->
                new BookNotFoundException("Book with id %s not found".formatted(id)));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream().map(bookConverter::modelToDto).toList();
    }

    @Override
    public BookDto insert(BookCreateDto bookCreateDto) {
        String title = bookCreateDto.getTitle();
        String authorId = bookCreateDto.getAuthorId();
        Set<String> genresIds = bookCreateDto.getGenreIds();
        return save(null, title, authorId, genresIds);

    }

    @Override
    public BookDto update(BookUpdateDto bookUpdateDto) {
        String id = bookUpdateDto.getId();
        if (findById(id) != null) {
            String title = bookUpdateDto.getTitle();
            String authorId = bookUpdateDto.getAuthorId();
            Set<String> genresIds = bookUpdateDto.getGenreIds();

            return save(id, title, authorId, genresIds);
        }
        throw new BookNotFoundException("Book with id %s not found".formatted(id));
    }

    @Override
    public void deleteById(String id) {
        deleteBookAndCascadeDeleteComments(id);
    }

    private BookDto save(String id, String title, String authorId, Set<String> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AuthorNotFoundException("Author with id %s not found".formatted(authorId)));
        var genres = genreRepository.findAllByIdIn(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new GenreNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        var book = new Book(id, title, author, genres);
        if (id != null) {
            updateCommentsReferenceInBook(book);
        }
        return bookConverter.modelToDto(bookRepository.save(book));
    }

    private void updateCommentsReferenceInBook(Book book) {
        var comments = commentRepository.findAllByBookId(book.getId());
        book.setComments(comments);
    }

    private void deleteBookAndCascadeDeleteComments(String bookId) {
        bookRepository.deleteById(bookId);
        commentRepository.deleteAllByBookId(bookId);
    }
}
