package ru.otus.hw.exceptions;

public class EntityAlreadyExistsException extends LibraryException {
    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}
