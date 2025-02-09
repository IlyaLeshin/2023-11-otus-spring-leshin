package ru.otus.spring.integration.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import ru.otus.spring.integration.domain.stages.Adult;
import ru.otus.spring.integration.domain.stages.Egg;
import ru.otus.spring.integration.domain.stages.Nymph;

@Service
@Slf4j
public class IncompleteMetamorphosisServiceImpl implements IncompleteMetamorphosisService {

    @Override
    public Nymph transform(Egg egg) {
        log.info("Egg stage ended:{}", egg);
        delay();
        Nymph nymph = new Nymph(egg.id(), egg.type());
        log.info("Nymph stage started:{}", nymph);
        return nymph;
    }

    @Override
    public Adult transform(Nymph nymph) {
        log.info("Nymph stage ended:{}", nymph.toString());
        delay();
        Adult adult = new Adult(nymph.id(), nymph.type());
        log.info("Adult stage started:{}", adult);
        return adult;

    }

    private static void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
