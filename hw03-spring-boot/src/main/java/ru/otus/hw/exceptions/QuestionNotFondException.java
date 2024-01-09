package ru.otus.hw.exceptions;

public class QuestionNotFondException extends RuntimeException {
    public QuestionNotFondException(String message, Throwable ex) {
        super(message, ex);
    }
}