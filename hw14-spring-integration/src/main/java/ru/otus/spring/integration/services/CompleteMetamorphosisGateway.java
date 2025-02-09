package ru.otus.spring.integration.services;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.spring.integration.domain.stages.Adult;
import ru.otus.spring.integration.domain.stages.Egg;
import ru.otus.spring.integration.domain.stages.Larva;
import ru.otus.spring.integration.domain.stages.Pupa;

import java.util.Collection;

@MessagingGateway
public interface CompleteMetamorphosisGateway {

    @Gateway(requestChannel = "eggsWithCompleteMetamorphismChannel", replyChannel = "larvaChannel")
    Collection<Larva>eggsMetamorphosis(Collection<Egg> eggs);

    @Gateway(requestChannel = "larvaChannel", replyChannel = "pupaChannel")
    Collection<Pupa> larvasMetamorphosis(Collection<Larva> larvas);

    @Gateway(requestChannel = "pupaChannel")
    Collection<Adult> pupasMetamorphosis(Collection<Pupa> pupas);
}
