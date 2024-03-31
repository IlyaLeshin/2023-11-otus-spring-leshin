package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.exceptions.CommentNotFoundException;
import ru.otus.hw.models.Book;
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
    public Optional<CommentDto> findById(String id) {
        return commentRepository.findById(id).map(commentConverter::modelToDto);
    }

    @Override
    public List<CommentDto> findAllByBookId(String bookId) {
        if (bookRepository.findById(bookId).isPresent()) {
            return commentRepository.findAllByBookId(bookId).stream().map(commentConverter::modelToDto).toList();
        }
        throw new BookNotFoundException("Book with id %s not found".formatted(bookId));
    }

    @Override
    public CommentDto create(String text, String bookId) {
        return save(null, text, bookId);
    }

    @Override
    public CommentDto update(String id, String text, String bookId) {
        if (findById(id).isPresent()) {
            return save(id, text, bookId);
        }
        throw new CommentNotFoundException("Comment with id %s to the book with id %s not found".formatted(id, bookId));
    }

    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    private CommentDto save(String id, String text, String bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with id %s not found".formatted(bookId)));
        var comment = commentRepository.save(new Comment(id, text, book));
        updateCommentsReferenceInBook(book);
        return commentConverter.modelToDto(comment);
    }

    private void updateCommentsReferenceInBook(Book book) {
        var comments = commentRepository.findAllByBookId(book.getId());
        book.setComments(comments);
        bookRepository.save(book);
    }
}
