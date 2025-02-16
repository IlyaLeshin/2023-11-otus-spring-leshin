package ru.otus.hw.controllers.page;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@AllArgsConstructor
public class CommentPageController {

    @GetMapping("/books/{bookId}/comments/{commentId}")
    public String commentPage(@PathVariable("bookId") String bookId, @PathVariable("commentId") long commentId,
                              Model model) {
        model.addAttribute("bookId", bookId);
        model.addAttribute("commentId", commentId);
        return "comments/comment";
    }

    @GetMapping("/books/{bookId}/comments/creation-form")
    public String creationFormCommentPage(@PathVariable("bookId") String bookId, Model model) {
        model.addAttribute("bookId", bookId);
        model.addAttribute("marker", "creation");
        return "comments/editing-form";
    }

    @GetMapping("/books/{bookId}/comments/{commentId}/editing-form")
    public String editingFormCommentPage(@PathVariable("bookId") String bookId,
                                         @PathVariable("commentId") String commentId,
                                         Model model) {
        model.addAttribute("bookId", bookId);
        model.addAttribute("commentId", commentId);
        return "comments/editing-form";
    }
}