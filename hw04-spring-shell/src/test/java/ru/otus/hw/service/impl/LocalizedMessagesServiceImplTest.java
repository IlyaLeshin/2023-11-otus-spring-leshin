package ru.otus.hw.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.LocaleConfig;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DisplayName("The test LocalizedMessagesServiceImpl should ")
@SpringBootTest(classes = {LocalizedMessagesServiceImpl.class})
@ImportAutoConfiguration(MessageSourceAutoConfiguration.class)
class LocalizedMessagesServiceImplTest {

    @Autowired
    private LocalizedMessagesServiceImpl localizedMessagesService;

    @MockBean
    private LocaleConfig localeConfig;

    @Test
    @DisplayName("Correct getting a message with the en-EN locale. current method: getMessage(String code, Object ...args)")
    void getMessage_en_US() {
        when(localeConfig.getLocale()).thenReturn(Locale.forLanguageTag("en-US"));
        String actualLocalizedMessage = localizedMessagesService.getMessage("TestService.answer.the.questions");

        assertEquals("Please answer the questions below", actualLocalizedMessage);
    }

    @Test
    @DisplayName("Correct getting a message with the ru-RU locale. current method: getMessage(String code, Object ...args)")
    void getMessage_ru_RU() {
        when(localeConfig.getLocale()).thenReturn(Locale.forLanguageTag("ru-RU"));
        String localizedMessage = localizedMessagesService.getMessage("TestService.answer.the.questions");

        assertEquals("Пожалуйста, ответьте на вопросы ниже", localizedMessage);
    }
}