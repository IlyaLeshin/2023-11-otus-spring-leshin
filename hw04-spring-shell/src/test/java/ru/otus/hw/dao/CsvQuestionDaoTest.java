package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.loader.Loader;
import ru.otus.hw.dao.parser.Parser;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.when;

@DisplayName("The test CsvQuestionDao should ")
@SpringBootTest(classes = CsvQuestionDao.class)
public class CsvQuestionDaoTest {

    @Autowired
    private QuestionDao questionDao;

    @MockBean
    private TestFileNameProvider testFileNameProvider;

    @MockBean
    private Loader loader;

    @MockBean
    private Parser parser;

    @Test
    @DisplayName("find all questions and answers from resource. current method: findAll()")
    void findAllTest() {
        Question[] expectedQuestionArr = getQuestionsArr();
        when(parser.parse(loader.loadData(testFileNameProvider.getTestFileName())))
                .thenReturn(Arrays.stream(expectedQuestionArr).toList());
        List<Question> actualQuestionList = questionDao.findAll();

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