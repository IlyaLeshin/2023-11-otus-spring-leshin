package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.loader.Loader;
import ru.otus.hw.dao.parser.Parser;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.impl.StreamsIOService;
import ru.otus.hw.service.impl.TestServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@DisplayName("The test TestServiceImpl should ")
@ExtendWith(MockitoExtension.class)
public class TestServiceImplTest {

    @InjectMocks
    private CsvQuestionDao csvQuestionDao;

    @Mock
    private Parser parser;

    @Mock
    private Loader loader;

    @Mock
    private TestFileNameProvider testFileNameProvider;

    @Mock
    IOService ioService;

    @BeforeEach
    void setUp() {
        ioService = mock(StreamsIOService.class);
    }

    @Test
    @DisplayName("get correct TestResult. current method: executeTestFor(Student student)")
    void executeTestForTest() {
        int expectedTestResultRightAnswersCount = 1;

        List<Answer> answersForQuestionOne = new ArrayList<>();
        answersForQuestionOne.add(new Answer("Answer #1 for question #1", true));
        answersForQuestionOne.add(new Answer("Answer #2 for question #1", false));
        answersForQuestionOne.add(new Answer("Answer #3 for question #1", false));

        List<Answer> answersForQuestionTwo = new ArrayList<>();
        answersForQuestionTwo.add(new Answer("Answer #1 for question #2", false));
        answersForQuestionTwo.add(new Answer("Answer #2 for question #2", true));

        Question questionOne = new Question("Question #1", answersForQuestionOne);
        Question questionTwo = new Question("Question #2", answersForQuestionTwo);

        List<Question> questionList = Arrays.stream(new Question[]{questionOne, questionTwo}).toList();

        Mockito.when(csvQuestionDao.findAll()).thenReturn(questionList);

        Mockito.when(ioService.readIntForRangeWithPrompt(1, 3, "Enter your answer number:",
                        "Your answer number is outside the range of available answers, try again:"))
                .thenReturn(2);
        Mockito.when(ioService.readIntForRangeWithPrompt(1, 2, "Enter your answer number:",
                        "Your answer number is outside the range of available answers, try again:"))
                .thenReturn(2);

        TestService testService = new TestServiceImpl(ioService, csvQuestionDao);
        Student student = new Student("testFirstName", "TestLastName");
        TestResult actualTestResult = testService.executeTestFor(student);

        assertEquals(expectedTestResultRightAnswersCount, actualTestResult.getRightAnswersCount());
    }
}