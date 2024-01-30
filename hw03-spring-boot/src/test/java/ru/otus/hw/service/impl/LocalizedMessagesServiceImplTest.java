package ru.otus.hw.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import ru.otus.hw.config.LocaleConfig;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("The test LocalizedMessagesServiceImpl should ")
@ExtendWith(MockitoExtension.class)
class LocalizedMessagesServiceImplTest {

    private final MessageSource messageSource = messageSource();

    @InjectMocks
    private LocalizedMessagesServiceImpl localizedMessagesService;

    @Mock
    private LocaleConfig localeConfig;

    @Test
    @DisplayName("Correct getting a message with the en-EN locale. current method: getMessage(String code, Object ...args)")
    void getMessage_en_US() {
        Mockito.when(localeConfig.getLocale()).thenReturn(Locale.forLanguageTag("en-US"));
        localizedMessagesService = new LocalizedMessagesServiceImpl(localeConfig, messageSource);
        String actualLocalizedMessage = localizedMessagesService.getMessage("TestService.answer.the.questions", messageSource, localeConfig.getLocale());

        assertEquals("Please answer the questions below", actualLocalizedMessage);
    }

    @Test
    @DisplayName("Correct getting a message with the ru-RU locale. current method: getMessage(String code, Object ...args)")
    void getMessage_ru_RU() {
        Mockito.when(localeConfig.getLocale()).thenReturn(Locale.forLanguageTag("ru-RU"));
        localizedMessagesService = new LocalizedMessagesServiceImpl(localeConfig, messageSource);
        String localizedMessage = localizedMessagesService.getMessage("TestService.answer.the.questions");

        assertEquals("Пожалуйста, ответьте на вопросы ниже", localizedMessage);
    }

    private MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        return messageSource;
    }
}