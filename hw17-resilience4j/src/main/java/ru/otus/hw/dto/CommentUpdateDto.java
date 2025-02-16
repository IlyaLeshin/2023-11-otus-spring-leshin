package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CommentUpdateDto {
    private long id;

    @NotBlank(message = "{text-field-should-not-be-blank}")
    @Size(min = 1, max = 254, message = "{text-field-should-has-expected-size}")
    private String text;

    @NotNull(message = "{bookId-field-should-not-be-blank}")
    private Long bookId;
}