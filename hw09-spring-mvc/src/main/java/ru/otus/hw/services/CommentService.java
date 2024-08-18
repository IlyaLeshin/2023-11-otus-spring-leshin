package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    CommentDto findById(String id);

    List<CommentDto> findAllByBookId(String bookId);

    CommentDto create(CommentDto commentDto);

    CommentDto update(CommentDto commentDto);

    void deleteById(String id);
}
