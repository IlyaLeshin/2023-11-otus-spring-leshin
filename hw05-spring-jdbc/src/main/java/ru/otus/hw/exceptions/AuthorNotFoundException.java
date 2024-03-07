package ru.otus.hw.exceptions;

public class AuthorNotFoundException extends EntityNotFoundException {
    public AuthorNotFoundException(String message) {
        super(message);
    }
}
