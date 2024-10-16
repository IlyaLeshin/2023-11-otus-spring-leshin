package ru.otus.hw.controllers.page;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@AllArgsConstructor
public class BookPageController {

    @GetMapping("/books")
    public String booksPage(Model model) {
        return "books/list-of-books";
    }

    @GetMapping("/books/{id}")
    public String bookWithCommentsPage(@PathVariable long id, Model model) {
        model.addAttribute("bookId", id);
        return "books/book-with-comments";
    }

    @GetMapping("/books/creation-form")
    public String createBookPage(Model model) {
        model.addAttribute("marker", "creation");
        return "books/editing-form";
    }

    @GetMapping("/books/{id}/editing-form")
    public String editBookPage(@PathVariable long id, Model model) {
        model.addAttribute("bookId", id);
        return "books/editing-form";
    }
}