package ru.otus.hw.exceptions;

public class GenreNotFoundException extends EntityNotFoundException {
    public GenreNotFoundException(String message) {
        super(message);
    }
}
