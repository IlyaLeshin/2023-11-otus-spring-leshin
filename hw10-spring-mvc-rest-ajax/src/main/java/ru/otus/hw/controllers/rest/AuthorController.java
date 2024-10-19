package ru.otus.hw.controllers.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;


import java.util.List;

@RestController
@AllArgsConstructor
public class AuthorController {
    private final AuthorService service;

    @GetMapping("/api/v1/authors")
    public List<AuthorDto> getListAuthors() {
        return service.findAll();
    }

}