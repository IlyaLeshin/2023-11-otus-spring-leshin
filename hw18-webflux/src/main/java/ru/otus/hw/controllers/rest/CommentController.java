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
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentUpdateDto;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.exceptions.CommentNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

@RestController
@AllArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentConverter commentConverter;

    @GetMapping("/api/v1/books/{bookId}/comments/{commentId}")
    public Mono<ResponseEntity<CommentDto>> getComment(@PathVariable("commentId") String commentId) {
        return commentRepository.findById(commentId)
                .map(commentConverter::modelToDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @PostMapping("/api/v1/books/{bookId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CommentDto> createComment(@Valid @RequestBody CommentCreateDto commentCreateDto) {
        String text = commentCreateDto.getText();
        String bookId = commentCreateDto.getBookId();

        return save(null, text, bookId);
    }

    @PutMapping("/api/v1/books/{bookId}/comments/{commentId}")
    public Mono<CommentDto> editComment(@Valid @RequestBody CommentUpdateDto commentUpdateDto) {
        String id = commentUpdateDto.getId();
        String text = commentUpdateDto.getText();
        String bookId = commentUpdateDto.getBookId();

        return save(id, text, bookId)
                .switchIfEmpty(Mono.error(new CommentNotFoundException("Comment with id %s to the book with id %s not found"
                        .formatted(id, bookId))));
    }

    @DeleteMapping("/api/v1/books/{bookId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteComment(@PathVariable("commentId") String commentId) {
        return commentRepository.deleteById(commentId);
    }


    private Flux<CommentDto> findAllByBookId(String bookId) {
        return commentRepository.findAllByBookId(bookId).map(commentConverter::modelToDto);
    }

    private Mono<CommentDto> save(String id, String text, String bookId) {
        return bookRepository.findById(bookId)
                .switchIfEmpty(Mono.error(new BookNotFoundException("Book with id %s not found"
                        .formatted(bookId))))
                .flatMap(book -> commentRepository.save(new Comment(id, text, book)))
                .map(commentConverter::modelToDto);

    }

}