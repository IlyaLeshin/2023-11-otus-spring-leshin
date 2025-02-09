package ru.otus.spring.integration.services;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.spring.integration.domain.stages.Egg;

import java.util.Collection;


@MessagingGateway
public interface IncubatingGateway {

    @Gateway(requestChannel = "eggsChannel")
    Collection<Egg> incubateEggs(Collection<Egg> eggs);
}
