package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.exceptions.CommentNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentConverter commentConverter;

    @Override
    public Optional<CommentDto> findById(long id) {
        return commentRepository.findById(id).map(commentConverter::modelToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findAllByBookId(long bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with id %d not found".formatted(bookId)));
        return book.getComments().stream().map(commentConverter::modelToDto).toList();
    }

    @Override
    @Transactional
    public CommentDto create(String text, long bookId) {
        return save(0, text, bookId);
    }

    @Override
    @Transactional
    public CommentDto update(long id, String text, long bookId) {
        if (findById(id).isPresent()) {
            return save(id, text, bookId);
        }
        throw new CommentNotFoundException("Comment with id %d to the book with id %d not found".formatted(id, bookId));
    }

    @Override
    @Transactional
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
