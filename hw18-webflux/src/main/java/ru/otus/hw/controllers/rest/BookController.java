package ru.otus.hw.controllers.rest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.BookWithCommentsDto;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@AllArgsConstructor
public class BookController {
    private final BookService service;

    @GetMapping("/api/v1/books")
    @ResponseStatus(HttpStatus.OK)
    public List<BookDto> getListBooks() {
        return service.findAll();
    }

    @GetMapping("/api/v1/books/{id}")
    public BookWithCommentsDto getBookWithComments(@PathVariable String id) {
        return service.findWithCommentsById(id);
    }

    @PostMapping("/api/v1/books")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto createBook(@Valid @RequestBody BookCreateDto bookCreateDto) {
        return service.insert(bookCreateDto);
    }

    @PutMapping("/api/v1/books/{id}")
    public BookDto editBook(@Valid @RequestBody BookUpdateDto bookUpdateDto) {
        return service.update(bookUpdateDto);
    }

    @DeleteMapping("/api/v1/books/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable String id) {
        service.deleteById(id);
    }
}