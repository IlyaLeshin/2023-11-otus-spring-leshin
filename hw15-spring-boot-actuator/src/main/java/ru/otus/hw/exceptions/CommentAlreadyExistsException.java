package ru.otus.hw.exceptions;

public class CommentAlreadyExistsException extends EntityAlreadyExistsException {
    public CommentAlreadyExistsException(String message) {
        super(message);
    }
}
