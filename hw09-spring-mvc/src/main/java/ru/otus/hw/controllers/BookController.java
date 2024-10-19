/*
package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import jakarta.validation.Valid;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.services.BookService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final BookConverter bookConverter;

    @GetMapping("/")
    public String index() {
        return "redirect:/books/list";
    }

    @GetMapping("/books/list")
    public String booksListPage(Model model) {
        List<BookDto> books = bookService.findAll();
        model.addAttribute("books", books);
        return "books/list";
    }

    @GetMapping("books/edit")
    public String editPage(@RequestParam("id") String id, Model model) {
        BookDto bookDto = bookService.findById(id);
        model.addAttribute("book", bookDto);
        return "books/edit";
    }

    @PostMapping("books/create")
    public String saveBook(@Valid @ModelAttribute("book") BookDto bookDto,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "books/create";
        }
        bookService.update(bookDto);
        return "redirect:/";
    }

    @DeleteMapping("books/delete")
    public String deleteBook(@Valid @ModelAttribute("book") BookDto bookDto,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "books/delete";
        }
        bookService.deleteById(bookDto.getId());
        return "redirect:/";
    }
}
*/

package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Controller
@AllArgsConstructor
public class BookController {
    private final BookService service;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final BookConverter bookConverter;

    @GetMapping("/books")
    public String booksPage(Model model) {
        List<BookDto> books = service.findAll();
        model.addAttribute("books", books);
        return "books/books";
    }

    @GetMapping("/books/{id}")
    public String bookWithCommentsPage(@PathVariable String id, Model model) {
        var book = service.findWithCommentsById(id);
        model.addAttribute("book", book);
        return "books/book-with-comments";
    }

    @GetMapping("/books/create")
    public String createPage(Model model) {
        model.addAttribute("book", new BookCreateDto());
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("marker", "create");
        return "books/edit";
    }

    @PostMapping("/books/create")
    public String createBook(@Valid @ModelAttribute("book") BookCreateDto bookCreateDto,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", bookCreateDto);
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());
            return "books/edit";
        }
        service.insert(bookCreateDto);
        return "redirect:/books";
    }

    @GetMapping("/books/{id}/edit")
    public String editPage(@PathVariable("id") String id, Model model) {
        BookDto book = service.findById(id);
        model.addAttribute("book", bookConverter.dtoToUpdateDto(book));
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "books/edit";
    }

    @PostMapping("/books/{id}/edit")
    public String editBook(@Valid @ModelAttribute("book") BookUpdateDto bookUpdateDto,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", bookUpdateDto);
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());
            return "books/edit";
        }

        service.update(bookUpdateDto);
        return "redirect:/books";
    }

    @PostMapping("/books/{id}/delete")
    public String delete(@PathVariable String id) {
        service.deleteById(id);
        return "redirect:/books";
    }
}