package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BookWithCommentsDto {
    private String id;

    private String title;

    private AuthorDto authorDto;

    private List<GenreDto> genreDtoList;

    private List<CommentDto> commentDtoList;
}
