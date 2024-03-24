package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.BookWithCommentsConverter;
import ru.otus.hw.services.BookService;

import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@ShellComponent
public class BookCommands {

    private final BookService bookService;

    private final BookConverter bookConverter;

    private final BookWithCommentsConverter bookWithCommentsConverter;

    // ab
    @ShellMethod(value = "Find all books", key = "ab")
    public String findAllBooks() {
        return bookService.findAll().stream()
                .map(bookConverter::dtoToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    // bbid 1
    @ShellMethod(value = "Find book by id", key = "bbid")
    public String findBookById(String id) {
        return bookService.findById(id)
                .map(bookConverter::dtoToString)
                .orElse("Book with id %s not found".formatted(id));
    }

    // bbcid 1
    @ShellMethod(value = "Find book with comments by id", key = "bbcid")
    public String findBookWithCommentsById(String id) {
        return bookService.findWithCommentsById(id)
                .map(bookWithCommentsConverter::dtoToString)
                .orElse("Book with id %s not found".formatted(id));
    }

    // bins newBook 1 1,6
    @ShellMethod(value = "Insert book", key = "bins")
    public String insertBook(String title, String authorId, Set<String> genresIds) {
        var savedBook = bookService.insert(title, authorId, genresIds);
        return bookConverter.dtoToString(savedBook);
    }

    // bupd 1 editedBook 65ff4ebad3bc0a781cdef982 65ff4ebad3bc0a781cdef98b,65ff4ebad3bc0a781cdef989
    @ShellMethod(value = "Update book", key = "bupd")
    public String updateBook(String id, String title, String authorId, Set<String> genresIds) {
        var savedBook = bookService.update(id, title, authorId, genresIds);
        return bookConverter.dtoToString(savedBook);
    }

    // bdel 4
    @ShellMethod(value = "Delete book by id", key = "bdel")
    public void deleteBook(String id) {
        bookService.deleteById(id);
    }
}
