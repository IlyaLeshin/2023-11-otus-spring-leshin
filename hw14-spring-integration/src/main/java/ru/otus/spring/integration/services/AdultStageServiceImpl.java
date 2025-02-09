package ru.otus.spring.integration.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.spring.integration.domain.stages.Adult;

@Service
@Slf4j
public class AdultStageServiceImpl implements AdultStageService {

    @Override
    public void releaseIntoWild(Adult adult) {
        delay();
        log.info("Adult insect released into the wild:{}", adult.toString());

    }

    private static void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
