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

    // bbid b1
    @ShellMethod(value = "Find book by id", key = "bbid")
    public String findBookById(String id) {
        return bookService.findById(id)
                .map(bookConverter::dtoToString)
                .orElse("Book with id %s not found".formatted(id));
    }

    // bbcid b1
    @ShellMethod(value = "Find book with comments by id", key = "bbcid")
    public String findBookWithCommentsById(String id) {
        return bookService.findWithCommentsById(id)
                .map(bookWithCommentsConverter::dtoToString)
                .orElse("Book with id %s not found".formatted(id));
    }

    // bins newBook a1 g1,g6
    @ShellMethod(value = "Insert book", key = "bins")
    public String insertBook(String title, String authorId, Set<String> genresIds) {
        var savedBook = bookService.insert(title, authorId, genresIds);
        return bookConverter.dtoToString(savedBook);
    }

    // bupd b1 editedBook a2 g5,g4
    @ShellMethod(value = "Update book", key = "bupd")
    public String updateBook(String id, String title, String authorId, Set<String> genresIds) {
        var savedBook = bookService.update(id, title, authorId, genresIds);
        return bookConverter.dtoToString(savedBook);
    }

    // bdel b3
    @ShellMethod(value = "Delete book by id", key = "bdel")
    public void deleteBook(String id) {
        bookService.deleteById(id);
    }
}
