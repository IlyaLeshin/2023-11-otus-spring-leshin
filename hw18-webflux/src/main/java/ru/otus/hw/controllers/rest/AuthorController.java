package ru.otus.hw.controllers.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.repositories.AuthorRepository;

@RestController
@AllArgsConstructor
public class AuthorController {

    private final AuthorRepository authorRepository;

    private final AuthorConverter authorConverter;

    @GetMapping("/api/v1/authors")
    public Flux<AuthorDto> getListAuthors() {
        return authorRepository.findAll().map(authorConverter::modelToDto);
    }

}