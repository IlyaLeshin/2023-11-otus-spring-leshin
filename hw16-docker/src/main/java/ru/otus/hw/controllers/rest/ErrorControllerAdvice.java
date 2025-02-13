package ru.otus.hw.controllers.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.hw.exceptions.EntityAlreadyExistsException;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.exceptions.LibraryException;

@RestControllerAdvice
@Slf4j
public class ErrorControllerAdvice {
    @ExceptionHandler(LibraryException.class)
    public ResponseEntity<String> libraryException(LibraryException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> notFoundException(EntityNotFoundException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<String> alreadyExistsException(EntityAlreadyExistsException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }

}