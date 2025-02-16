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
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentUpdateDto;
import ru.otus.hw.services.CommentService;

@RestController
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/api/v1/books/{bookId}/comments/{commentId}")
    public CommentDto getComment(@PathVariable("commentId") String commentId) {
        return commentService.findById(commentId);
    }

    @PostMapping("/api/v1/books/{bookId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@Valid @RequestBody CommentCreateDto commentCreateDto) {
        return commentService.create(commentCreateDto);
    }

    @PutMapping("/api/v1/books/{bookId}/comments/{commentId}")
    public CommentDto editComment(@Valid @RequestBody CommentUpdateDto commentUpdateDto) {
        return commentService.update(commentUpdateDto);
    }

    @DeleteMapping("/api/v1/books/{bookId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable("commentId") String commentId) {
        commentService.deleteById(commentId);
    }
}