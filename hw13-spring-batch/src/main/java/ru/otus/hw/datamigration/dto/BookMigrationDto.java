package ru.otus.hw.datamigration.dto;

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
public class BookMigrationDto {
    private String id;

    private String title;

    private String authorId;

    private List<String> genreIds;
}
