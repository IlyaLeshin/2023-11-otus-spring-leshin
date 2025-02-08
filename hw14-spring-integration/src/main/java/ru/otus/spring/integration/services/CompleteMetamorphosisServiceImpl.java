package ru.otus.spring.integration.services;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import ru.otus.spring.integration.domain.stages.Adult;
import ru.otus.spring.integration.domain.stages.Egg;
import ru.otus.spring.integration.domain.stages.Larva;
import ru.otus.spring.integration.domain.stages.Pupa;

@Service
@Slf4j
public class CompleteMetamorphosisServiceImpl implements CompleteMetamorphosisService {


    @Override
    public Larva transform(Egg egg) {
        log.info("ended Egg stage:{}", egg.toString());
        delay();
        Larva larva = new Larva(egg.id(), egg.type());
        log.info("started Larva stage:{}", larva.toString());
        return larva;
    }

    @Override
    public Pupa transform(Larva larva) {
        log.info("ended Larva stage:{}", larva.toString());
        delay();
        Pupa pupa = new Pupa(larva.id(), larva.type());
        log.info("started Pupa stage:{}", pupa.toString());
        return pupa;
    }

    @Override
    public Adult transform(Pupa pupa) {
        log.info("Pupa stage ended:{}", pupa.toString());
        delay();
        Adult adult = new Adult(pupa.id(), pupa.type());
        log.info("Adult stage started:{}", adult.toString());
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
