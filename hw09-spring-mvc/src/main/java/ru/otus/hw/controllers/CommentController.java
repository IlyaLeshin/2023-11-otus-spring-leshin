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
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.services.CommentService;

@Controller
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/books/{bookId}/comments/{commentId}")
    public String commentPage(@PathVariable("commentId") String commentId,
                              Model model) {
        var commentDto = commentService.findById(commentId);
        model.addAttribute("comment", commentDto);
        return "comments/comment";
    }

    @GetMapping("/books/{bookId}/comments/create")
    public String createPage(@PathVariable("bookId") String bookId, Model model) {
        model.addAttribute("comment", new CommentDto(null,"",bookId));
        return "comments/create";
    }

    @PostMapping("/books/{bookId}/comments/create")
    public String createComment(@Valid @ModelAttribute("comment") CommentDto commentDto,
                                BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("comment", new CommentDto());
            return "comments/create";
        }
        commentService.create(commentDto);
        return "redirect:/books/{bookId}";
    }

    @GetMapping("/books/{bookId}/comments/{commentId}/edit")
    public String editPage(@PathVariable("commentId") String commentId, Model model) {
        CommentDto commentDto = commentService.findById(commentId);
        model.addAttribute("comment", commentDto);
        return "comments/edit";
    }

    @PostMapping("/books/{bookId}/comments/{commentId}/edit")
    public String editBook(@Valid @ModelAttribute("book") CommentDto commentDto,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("comment", commentDto);
            return "comments/edit";
        }

        commentService.update(commentDto);
        return "redirect:/books/{bookId}";
    }

    @PostMapping("/books/{bookId}/comments/{commentId}/delete")
    public String delete(@PathVariable("commentId") String commentId) {
        commentService.deleteById(commentId);
        return "redirect:/books/{bookId}";
    }
}