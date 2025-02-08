package ru.otus.spring.integration.services;

import java.util.ArrayList;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import ru.otus.spring.integration.domain.InsectType;
import ru.otus.spring.integration.domain.MetamorphosisType;
import ru.otus.spring.integration.domain.stages.Egg;

@Service
@Slf4j
@RequiredArgsConstructor
public class EggIncubationServiceImpl implements EggIncubationService {
    private static final InsectType[] INSECT_TYPE = {
            new InsectType(0L, "butterfly", MetamorphosisType.COMPLETE),
            new InsectType(1L, "grasshopper", MetamorphosisType.INCOMPLETE),
            new InsectType(2L, "bumblebee", MetamorphosisType.COMPLETE)};


    private static AtomicLong idSequence = new AtomicLong();

    private final IncubatingGateway incubatingGateway;

    @Override
    public void startGenerateEggsLoop() {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        for (int i = 0; i < 10; i++) {
            int num = i + 1;
            pool.execute(() -> {
                Collection<Egg> eggs = generateEggs();
                log.info("{}, New eggs added to incubator:{}", num, eggs.stream().map(Egg::toString)
                        .collect(Collectors.joining(",")));
                incubatingGateway.incubateEggs(eggs);
            });

            delay();
        }
    }

    private static Collection<Egg> generateEggs() {
        List<Egg> eggs = new ArrayList<>();
        for (int i = 0; i < random(2,5); ++i) {
            eggs.add(generateEgg());
        }
        return eggs;
    }

    private static Egg generateEgg() {
        return new Egg(getNextId(), INSECT_TYPE[random(0, INSECT_TYPE.length)]);
    }

    private static long getNextId() {
        return idSequence.incrementAndGet();
    }

    private void delay() {
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static int random(int start, int max) {
        return start == max ? start : (int) (start + Math.random() * (max - start));
    }
}
