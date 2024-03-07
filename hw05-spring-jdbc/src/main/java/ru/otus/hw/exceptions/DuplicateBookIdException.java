package ru.otus.hw.exceptions;

public class DuplicateBookIdException extends LibraryException {
    public DuplicateBookIdException(String message) {
        super(message);
    }
}
