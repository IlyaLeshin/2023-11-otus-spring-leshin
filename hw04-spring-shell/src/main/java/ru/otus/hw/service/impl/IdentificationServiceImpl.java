package ru.otus.hw.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Login;
import ru.otus.hw.service.IdentificationService;
import ru.otus.hw.service.LocalizedIOService;

@RequiredArgsConstructor
@Service
public class IdentificationServiceImpl implements IdentificationService {

    private final LocalizedIOService localizedIOService;

    @Override
    public Login identify() {
        var userName = localizedIOService.readStringWithPromptLocalized("IdentificationService.input.user.name");
        return new Login(userName);
    }
}
