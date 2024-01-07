package ru.otus.hw.dao.extractor;

import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.loader.Loader;
import ru.otus.hw.dao.parser.Parser;
import ru.otus.hw.domain.Question;

import java.util.List;

@Component
public class QuestionExtractorCsvFromResource implements QuestionExtractor {

    private final TestFileNameProvider fileNameProvider;

    private final Loader loader;

    private final Parser parser;

    public QuestionExtractorCsvFromResource(TestFileNameProvider fileNameProvider, Loader loader, Parser parser) {
        this.fileNameProvider = fileNameProvider;
        this.loader = loader;
        this.parser = parser;
    }

    @Override
    public List<Question> loadData() {
        return parser.parse(loader.loadData(fileNameProvider.getTestFileName()));
    }
}