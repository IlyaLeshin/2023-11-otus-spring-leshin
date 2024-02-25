package ru.otus.hw.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BookWithCommentsDto {
    private Long id;

    private String title;

    private AuthorDto authorDto;

    private List<GenreDto> genreDtoList = new ArrayList<>();

    private List<CommentDto> commentDtoList = new ArrayList<>();
}
