package ru.otus.hw.dao;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.extractor.QuestionExtractorCsvFromResource;
import ru.otus.hw.dao.loader.Loader;
import ru.otus.hw.dao.parser.Parser;
import ru.otus.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

    private final Loader loader;

    private final Parser parser;

    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        return new QuestionExtractorCsvFromResource(loader,parser,fileNameProvider).loadData();
    }
}
