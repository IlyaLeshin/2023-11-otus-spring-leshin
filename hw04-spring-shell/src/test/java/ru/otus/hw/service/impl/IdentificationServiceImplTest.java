package ru.otus.hw.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.domain.Login;
import ru.otus.hw.service.IdentificationService;
import ru.otus.hw.service.LocalizedIOService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DisplayName("The test IdentificationServiceImplTest should ")
@SpringBootTest(classes = {IdentificationServiceImpl.class})
public class IdentificationServiceImplTest {

    @Autowired
    private IdentificationService identificationService;

    @MockBean
    private LocalizedIOService localizedIOService;

    @Test
    @DisplayName("get correct login. current method: identify()")
    void identifyTest() {
        Login expectedLogin = new Login("testUserName");
        when(localizedIOService.readStringWithPromptLocalized("IdentificationService.input.user.name"))
                .thenReturn("testUserName");
        Login actualLogin = identificationService.identify();

        assertEquals(expectedLogin, actualLogin);
    }
}
