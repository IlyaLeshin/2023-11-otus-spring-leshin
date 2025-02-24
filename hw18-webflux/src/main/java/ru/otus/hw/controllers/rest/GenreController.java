package ru.otus.hw.controllers.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repositories.GenreRepository;

@RestController
@AllArgsConstructor
public class GenreController {
    private final GenreRepository genreRepository;

    private final GenreConverter genreConverter;

    @GetMapping("/api/v1/genres")
    public Flux<GenreDto> getListAuthors() {
        return genreRepository.findAll().map(genreConverter::modelToDto);
    }

}