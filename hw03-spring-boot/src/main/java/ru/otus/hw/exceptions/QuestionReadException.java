package ru.otus.hw.exceptions;

public class QuestionReadException extends QuestionException {
    public QuestionReadException(String message, Throwable ex) {
        super(message, ex);
    }
}