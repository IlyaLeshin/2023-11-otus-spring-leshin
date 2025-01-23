package ru.otus.hw.datamigration.processors;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import ru.otus.hw.datamigration.dto.BookMigrationDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

@Component
@AllArgsConstructor
public class BookProcessor implements ItemProcessor<Book, BookMigrationDto> {

    @Override
    public BookMigrationDto process(@NonNull Book item) throws Exception {
        try {
            return new BookMigrationDto(item.getId(), item.getTitle(),
                    item.getAuthor().getId(),
                    item.getGenres().stream().map(Genre::getId).toList());
        } catch (Exception e) {
            throw new Exception("Exception in MigrationBook process", e);
        }
    }
}
