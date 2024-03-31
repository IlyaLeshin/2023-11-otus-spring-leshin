package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {
    private final CommentService commentService;

    private final CommentConverter commentConverter;

    //acbbid b1
    @ShellMethod(value = "Find all comments by book id", key = "acbbid")
    public String findAllCommentsByBookId(String bookId) {
        return commentService.findAllByBookId(bookId).stream()
                .map(commentConverter::dtoToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    // cbid c1
    @ShellMethod(value = "Find comment by id", key = "cbid")
    public String findCommentById(String id) {
        return commentService.findById(id)
                .map(commentConverter::dtoToString)
                .orElse("Comment with id %s not found".formatted(id));
    }

    // cc new_comment b1
    @ShellMethod(value = "Create comment", key = "cc")
    public String createComment(String content, String bookId) {
        var saveComment = commentService.create(content, bookId);
        return commentConverter.dtoToString(saveComment);
    }

    // uc c1 changed_comment b1
    @ShellMethod(value = "Update comment", key = "uc")
    public String updateComment(String id, String content, String bookId) {
        var saveComment = commentService.update(id, content, bookId);
        return commentConverter.dtoToString(saveComment);
    }

    //dcbid c2
    @ShellMethod(value = "Delete comment by id", key = "dcbid")
    public void deleteComment(String id) {
        commentService.deleteById(id);
    }
}
