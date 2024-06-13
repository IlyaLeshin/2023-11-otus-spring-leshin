package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookWithCommentsDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {
    Optional<BookDto> findById(String id);

    Optional<BookWithCommentsDto> findWithCommentsById(String id);

    List<BookDto> findAll();

    BookDto insert(String title, String authorId, Set<String> genres);

    BookDto update(String id, String title, String authorId, Set<String> genres);

    void deleteById(String id);
}
