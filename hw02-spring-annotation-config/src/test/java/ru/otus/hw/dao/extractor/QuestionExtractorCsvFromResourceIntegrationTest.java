package ru.otus.hw.dao.extractor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.loader.Loader;
import ru.otus.hw.dao.loader.ResourceLoader;
import ru.otus.hw.dao.parser.Parser;
import ru.otus.hw.dao.parser.ParserCsv;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.mock;

@DisplayName("The integration test QuestionExtractorCsvFromResource should ")
@ExtendWith(MockitoExtension.class)
public class QuestionExtractorCsvFromResourceIntegrationTest {

    @Mock
    private TestFileNameProvider testFileNameProvider;

    @BeforeEach
    void setUp() {
        testFileNameProvider = mock(TestFileNameProvider.class);
    }

    @Test
    @DisplayName("load and parse questions and answers from resource. current method: loadData()")
    void loadData() {
        Question[] expectedQuestionArr = getQuestionsArr();

        Mockito.when(testFileNameProvider.getTestFileName()).thenReturn("test-questions.csv");

        Loader loader = new ResourceLoader();
        Parser parser = new ParserCsv();
        QuestionExtractor questionExtractor = new QuestionExtractorCsvFromResource(testFileNameProvider, loader, parser);

        List<Question> actualQuestionList = questionExtractor.loadData();

        assertArrayEquals(expectedQuestionArr, actualQuestionList.toArray());
    }

    private Question[] getQuestionsArr() {
        List<Answer> answersForQuestionOne = new ArrayList<>();
        List<Answer> answersForQuestionTwo = new ArrayList<>();

        answersForQuestionOne.add(new Answer("Answer #1 for question #1", true));
        answersForQuestionOne.add(new Answer("Answer #2 for question #1", false));
        answersForQuestionOne.add(new Answer("Answer #3 for question #1", false));

        answersForQuestionTwo.add(new Answer("Answer #1 for question #2", false));
        answersForQuestionTwo.add(new Answer("Answer #2 for question #2", true));

        Question questionOne = new Question("Question #1", answersForQuestionOne);
        Question questionTwo = new Question("Question #2", answersForQuestionTwo);

        return new Question[]{questionOne, questionTwo};
    }
}