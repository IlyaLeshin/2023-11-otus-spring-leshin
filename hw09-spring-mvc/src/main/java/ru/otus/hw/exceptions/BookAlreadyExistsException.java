package ru.otus.hw.exceptions;

public class BookAlreadyExistsException extends EntityAlreadyExistsException {
    public BookAlreadyExistsException(String message) {
        super(message);
    }
}
