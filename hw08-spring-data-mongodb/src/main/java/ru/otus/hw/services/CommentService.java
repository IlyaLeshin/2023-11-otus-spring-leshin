package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<CommentDto> findById(String id);

    List<CommentDto> findAllByBookId(String bookId);

    CommentDto create(String commentText, String bookId);

    CommentDto update(String id, String commentText, String bookId);

    void deleteById(String id);
}
