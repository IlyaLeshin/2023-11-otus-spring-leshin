package ru.otus.spring.integration.services;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.spring.integration.domain.stages.Adult;
import ru.otus.spring.integration.domain.stages.Egg;
import ru.otus.spring.integration.domain.stages.Nymph;

import java.util.Collection;

@MessagingGateway
public interface IncompleteMetamorphosisGateway {

    @Gateway(requestChannel = "eggsWithIncompleteMetamorphismChannel", replyChannel = "nymphChannel")
    Collection<Nymph>eggsMetamorphosis(Collection<Egg> eggs);

    @Gateway(requestChannel = "nymphChannel")
    Collection<Adult> nyphsMetamorphosis(Collection<Nymph> nymphs);
}
