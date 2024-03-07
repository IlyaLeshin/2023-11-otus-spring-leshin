package ru.otus.hw.exceptions;

public class QuestionNotFondException extends QuestionException {
    public QuestionNotFondException(String message, Throwable ex) {
        super(message, ex);
    }
}