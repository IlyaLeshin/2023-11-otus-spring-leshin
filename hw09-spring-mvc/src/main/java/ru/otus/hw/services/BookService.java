package ru.otus.hw.services;

import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.BookWithCommentsDto;

import java.util.List;

public interface BookService {
    BookDto findById(String id);

    BookWithCommentsDto findWithCommentsById(String id);

    List<BookDto> findAll();

    BookDto insert(BookCreateDto bookDto);

    BookDto update(BookUpdateDto bookDto);

    void deleteById(String id);
}
