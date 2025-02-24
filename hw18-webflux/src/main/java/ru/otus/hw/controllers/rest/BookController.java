package ru.otus.hw.controllers.rest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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

import java.util.Objects;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RestController
@AllArgsConstructor
public class BookController {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final BookConverter bookConverter;

    private final BookWithCommentsConverter bookWithCommentsConverter;

    @GetMapping("/api/v1/books")
    @ResponseStatus(HttpStatus.OK)
    public Flux<BookDto> getListBooks() {
        return bookRepository.findAll().map(bookConverter::modelToDto);
    }

    @GetMapping("/api/v1/books/{id}")
    public Mono<ResponseEntity<BookWithCommentsDto>> getBookWithComments(@PathVariable String id) {
        return bookRepository.findById(id)
                .map(bookWithCommentsConverter::modelToDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @PostMapping("/api/v1/books")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BookDto> createBook(@Valid @RequestBody BookCreateDto bookCreateDto) {
        String title = bookCreateDto.getTitle();
        String authorId = bookCreateDto.getAuthorId();
        Set<String> genresIds = bookCreateDto.getGenreIds();
        return save(null, title, authorId, genresIds);
    }

    @PutMapping("/api/v1/books/{id}")
    public Mono<BookDto> editBook(@Valid @RequestBody BookUpdateDto bookUpdateDto) {
        String id = bookUpdateDto.getId();
        String title = bookUpdateDto.getTitle();
        String authorId = bookUpdateDto.getAuthorId();
        Set<String> genresIds = bookUpdateDto.getGenreIds();

        return save(id, title, authorId, genresIds)
                .switchIfEmpty(Mono.error(new BookNotFoundException("Book with id %s not found".formatted(id))));
    }

    @DeleteMapping("/api/v1/books/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteBook(@PathVariable String id) {
        return deleteBookAndCascadeDeleteComments(id);
    }

    private Mono<BookDto> save(String id, String title, String authorId, Set<String> genresIds) {
        if (isEmpty(genresIds)) {
            return Mono.error(new IllegalArgumentException("Genres ids must not be null"));
        }
        var author = authorRepository.findById(authorId)
                .map(Objects::requireNonNull)
                .switchIfEmpty(Mono.error(new AuthorNotFoundException("Author with id %s not found"
                        .formatted(authorId))));

        var genres = genreRepository.findAllByIdIn(genresIds)
                .switchIfEmpty(Mono.error(new GenreNotFoundException("Genres with ids %s not found"
                        .formatted(genresIds))))
                .collectList();

        if (id != null) {
            var comments = commentRepository.findAllByBookId(id).collectList();
            return Mono.zip(author, genres, comments)
                    .flatMap(bookComponents -> {
                        var book = new Book(id, title, bookComponents.getT1(), bookComponents.getT2());
                        book.setComments(bookComponents.getT3());
                        return bookRepository.save(book);
                    })
                    .map(bookConverter::modelToDto);
        }
        return Mono.zip(author, genres, (authorMono, genresMono) -> new Book(null, title, authorMono, genresMono))
                .flatMap(bookRepository::save)
                .map(bookConverter::modelToDto);
    }

    private Mono<Void> deleteBookAndCascadeDeleteComments(String bookId) {
        return bookRepository.deleteById(bookId)
                .then(commentRepository.deleteAllByBookId(bookId));
    }
}