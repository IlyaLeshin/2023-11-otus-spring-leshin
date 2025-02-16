package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentUpdateDto;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.exceptions.CommentNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentConverter commentConverter;

    @Override
    @RateLimiter(name = "read-ratelimiter")
    @CircuitBreaker(name = "read-circuitbreaker")
    public CommentDto findById(long id) {
        return commentRepository.findById(id).map(commentConverter::modelToDto)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id %s not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    @RateLimiter(name = "mass-read-ratelimiter")
    @CircuitBreaker(name = "mass-read-circuitbreaker")
    public List<CommentDto> findAllByBookId(long bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with id %d not found".formatted(bookId)));
        return book.getComments().stream().map(commentConverter::modelToDto).toList();
    }

    @Override
    public CommentDto create(CommentCreateDto commentCreateDto) {
            String text = commentCreateDto.getText();
            long bookId = commentCreateDto.getBookId();
            return save(0L, text, bookId);

    }

    @Override
    @Retry(name = "update-repeater")
    public CommentDto update(CommentUpdateDto commentUpdateDto) {
        long id = commentUpdateDto.getId();
        if (findById(id) != null) {
            String text = commentUpdateDto.getText();
            long bookId = commentUpdateDto.getBookId();
            return save(id, text, bookId);
        }
        throw new CommentNotFoundException("Comment with id %s to the book with id %s not found"
                .formatted(id, commentUpdateDto.getBookId()));
    }

    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private CommentDto save(long id, String text, long bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with id %d not found".formatted(bookId)));
        var comment = new Comment(id, text, book);
        return commentConverter.modelToDto(commentRepository.save(comment));
    }
}
