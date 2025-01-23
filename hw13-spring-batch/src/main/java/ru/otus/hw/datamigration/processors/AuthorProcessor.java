package ru.otus.hw.datamigration.processors;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.datamigration.dto.AuthorMigrationDto;
import ru.otus.hw.models.Author;

@Component
@AllArgsConstructor
public class AuthorProcessor implements ItemProcessor<Author, AuthorMigrationDto>, StepExecutionListener {

    @Override
    public AuthorMigrationDto process(@NonNull Author item) throws Exception {
        try {
            return new AuthorMigrationDto(item.getId(), item.getFullName());

        } catch (Exception e) {
            throw new Exception("Exception in AuthorProcessor process()", e);
        }
    }

}

