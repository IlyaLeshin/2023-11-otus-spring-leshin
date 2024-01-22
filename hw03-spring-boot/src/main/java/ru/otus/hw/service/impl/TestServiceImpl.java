package ru.otus.hw.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.exceptions.QuestionNotFondException;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.TestService;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService localizedIOService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        localizedIOService.printLine("");
        localizedIOService.printLineLocalized("TestService.answer.the.questions");
        localizedIOService.printLine("");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            printCurrentQuestion(question);
            printAnswersToTheCurrentQuestion(question);
            var isAnswerValid = getStudentAnswerToTheCurrentQuestion(question);
            testResult.applyAnswer(question, isAnswerValid);
        }

        return testResult;
    }

    private void printCurrentQuestion(Question question) {
        try {
            localizedIOService.printLine("\n" + question.text());
        } catch (NullPointerException e) {
            throw new QuestionNotFondException("question not found", e);
        }
    }

    private void printAnswersToTheCurrentQuestion(Question question) {
        try {
            var answersToTheCurrentQuestion = question.answers();

            if (answersToTheCurrentQuestion != null) {
                for (int i = 0; i < answersToTheCurrentQuestion.size(); i++) {
                    localizedIOService.printFormattedLineLocalized("TestService.answer.number.and.text",
                            i + 1, answersToTheCurrentQuestion.get(i).text());
                }
            } else {
                localizedIOService.printLineLocalized("TestService.answer.not.found");
            }
        } catch (NullPointerException e) {
            throw new QuestionNotFondException("question not found", e);
        }
    }

    private boolean getStudentAnswerToTheCurrentQuestion(Question question) {
        try {
            var minNumberOfAvailableAnswers = 1;
            var maxNumberOfAvailableAnswers = question.answers().size();
            int studentAnswerNumber = localizedIOService.readIntForRangeWithPromptLocalized(minNumberOfAvailableAnswers,
                    maxNumberOfAvailableAnswers,
                    "TestService.student.answer.number.enter",
                    "TestService.student.answer.number.error");

            return question.answers().get(studentAnswerNumber - 1).isCorrect();
        } catch (NullPointerException e) {
            throw new QuestionNotFondException("question not found", e);
        }
    }
}