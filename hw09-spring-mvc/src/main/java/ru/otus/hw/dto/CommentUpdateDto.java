package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CommentUpdateDto {
    private String id;

    @NotBlank(message = "{text-field-should-not-be-blank}")
    @Size(min = 1, max = 254, message = "{text-field-should-has-expected-size}")
    private String text;

    @NotBlank(message = "{bookId-field-should-not-be-blank}")
    private String bookId;
}