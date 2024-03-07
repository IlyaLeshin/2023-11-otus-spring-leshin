package ru.otus.hw.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.ResultService;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final TestConfig testConfig;

    private final LocalizedIOService localizedIOService;

    @Override
    public void showResult(TestResult testResult) {
        localizedIOService.printLine("");
        localizedIOService.printLineLocalized("ResultService.test.results");
        localizedIOService.printFormattedLineLocalized("ResultService.student",
                testResult.getStudent().getFullName());
        localizedIOService.printFormattedLineLocalized("ResultService.answered.questions.count",
                testResult.getAnsweredQuestions().size());
        localizedIOService.printFormattedLineLocalized("ResultService.right.answers.count",
                testResult.getRightAnswersCount());

        if (testResult.getRightAnswersCount() >= testConfig.getRightAnswersCountToPass()) {
            localizedIOService.printLineLocalized("ResultService.passed.test");
            return;
        }
        localizedIOService.printLineLocalized("ResultService.fail.test");
    }
}