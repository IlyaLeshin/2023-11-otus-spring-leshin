package ru.otus.hw.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.impl.StreamsIOService;
import ru.otus.hw.service.impl.TestServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("The test TestServiceImpl should ")
@ExtendWith(MockitoExtension.class)
public class TestServiceImplTest {

    @InjectMocks
    private TestServiceImpl testService;

    @Mock
    private QuestionDao questionDao;

    @Mock
    LocalizedIOService localizedIOService;

    @Test
    @DisplayName("get correct TestResult. current method: executeTestFor(Student student)")
    void executeTestForTest() {
        int expectedTestResultRightAnswersCount = 1;

        List<Question> questionList = getQuestionsList();

        Mockito.when(questionDao.findAll()).thenReturn(questionList);
        Mockito.when(localizedIOService.readIntForRangeWithPromptLocalized(1, 3, "TestService.student.answer.number.enter",
                        "TestService.student.answer.number.error"))
                .thenReturn(2);
        Mockito.when(localizedIOService.readIntForRangeWithPromptLocalized(1, 2, "TestService.student.answer.number.enter",
                        "TestService.student.answer.number.error"))
                .thenReturn(2);

        Student student = new Student("testFirstName", "TestLastName");
        TestResult actualTestResult = testService.executeTestFor(student);

        assertEquals(expectedTestResultRightAnswersCount, actualTestResult.getRightAnswersCount());
    }

    private List<Question> getQuestionsList() {
        List<Answer> answersForQuestionOne = new ArrayList<>();
        List<Answer> answersForQuestionTwo = new ArrayList<>();

        answersForQuestionOne.add(new Answer("Answer #1 for question #1", true));
        answersForQuestionOne.add(new Answer("Answer #2 for question #1", false));
        answersForQuestionOne.add(new Answer("Answer #3 for question #1", false));

        answersForQuestionTwo.add(new Answer("Answer #1 for question #2", false));
        answersForQuestionTwo.add(new Answer("Answer #2 for question #2", true));

        Question questionOne = new Question("Question #1", answersForQuestionOne);
        Question questionTwo = new Question("Question #2", answersForQuestionTwo);

        List<Question> questionList = new ArrayList<>();
        questionList.add(questionOne);
        questionList.add(questionTwo);

        return questionList;
    }
}