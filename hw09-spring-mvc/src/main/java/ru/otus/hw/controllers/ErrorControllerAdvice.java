package ru.otus.hw.controllers;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.hw.exceptions.LibraryException;

@ControllerAdvice
public class ErrorControllerAdvice {
    @ExceptionHandler(LibraryException.class)
    public String errorPage(LibraryException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error";
    }
}