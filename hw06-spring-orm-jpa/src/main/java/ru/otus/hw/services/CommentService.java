package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<CommentDto> findById(long id);

    List<CommentDto> findAllByBookId(long bookId);

    CommentDto create(String commentText, long bookId);

    CommentDto update(long id, String commentText, long bookId);

    void deleteById(long id);
}
