package ru.otus.hw.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.loader.Loader;
import ru.otus.hw.dao.parser.Parser;
import ru.otus.hw.domain.Question;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

    private final TestFileNameProvider fileNameProvider;

    private final Loader loader;

    private final Parser parser;

    @Override
    public List<Question> findAll() {
        return parser.parse(loader.loadData(fileNameProvider.getTestFileName()));
    }
}