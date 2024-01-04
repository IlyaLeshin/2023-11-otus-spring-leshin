package ru.otus.hw.dao.extractor;

import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.loader.Loader;
import ru.otus.hw.dao.parser.Parser;
import ru.otus.hw.domain.Question;

import java.util.List;

@Component
public class QuestionExtractorCsvFromResource implements QuestionExtractor {

    private final Loader loader;

    private final Parser parser;

    private final TestFileNameProvider fileNameProvider;

    public QuestionExtractorCsvFromResource(Loader loader, Parser parser, TestFileNameProvider fileNameProvider) {
        this.loader = loader;
        this.parser = parser;
        this.fileNameProvider = fileNameProvider;
    }

    @Override
    public List<Question> loadData() {
        return parser.parse(loader.loadData(fileNameProvider.getTestFileName()));
    }
}
