package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.BookWithCommentsConverter;
import ru.otus.hw.dto.BookDto;
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
import java.util.Optional;
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
    public Optional<BookDto> findById(String id) {
        return bookRepository.findById(id).map(bookConverter::modelToDto);
    }

    @Override
    public Optional<BookWithCommentsDto> findWithCommentsById(String id) {
        return bookRepository.findById(id).map(bookWithCommentsConverter::modelToDto);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream().map(bookConverter::modelToDto).toList();
    }

    @Override
    public BookDto insert(String title, String authorId, Set<String> genresIds) {
        return save(null, title, authorId, genresIds);
    }

    @Override
    public BookDto update(String id, String title, String authorId, Set<String> genresIds) {
        if (findById(id).isPresent()) {
            return save(id, title, authorId, genresIds);
        }
        throw new BookNotFoundException("Book with id %s not found".formatted(id));
    }

    @Override
    public void deleteById(String id) {
        bookRepository.deleteById(id);
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
}
