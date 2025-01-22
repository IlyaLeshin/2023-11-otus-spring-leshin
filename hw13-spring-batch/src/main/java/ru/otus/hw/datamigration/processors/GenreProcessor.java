package ru.otus.hw.datamigration.processors;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.datamigration.dto.GenreMigrationDto;
import ru.otus.hw.models.Genre;

@Component
@AllArgsConstructor
public class GenreProcessor implements ItemProcessor<Genre, GenreMigrationDto> {

    @Override
    public GenreMigrationDto process(@NonNull Genre item) throws Exception {
        try {
            return new GenreMigrationDto(item.getId(), item.getName());
        } catch (Exception e) {
            throw new Exception("Exception in GenreProcessor process()", e);
        }
    }

}