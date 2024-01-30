package ru.otus.hw.exceptions;

public class QuestionException extends RuntimeException {
    public QuestionException(String message, Throwable ex) {
        super(message, ex);
    }
}