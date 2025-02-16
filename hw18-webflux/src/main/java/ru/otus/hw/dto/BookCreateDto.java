package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BookCreateDto {
    @NotBlank(message = "{name-field-should-not-be-blank}")
    @Size(min = 2, max = 20, message = "{name-field-should-has-expected-size}")
    private String title;

    @NotNull(message = "{authorId-field-should-not-be-blank}")
    private Long authorId;

    @NotNull
    @Size(min = 1,message = "{genresIds-field-has-expected-size}")
    private Set<Long> genreIds;
}
