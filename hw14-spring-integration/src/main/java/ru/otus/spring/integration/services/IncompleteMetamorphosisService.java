package ru.otus.spring.integration.services;

import ru.otus.spring.integration.domain.stages.Adult;
import ru.otus.spring.integration.domain.stages.Egg;
import ru.otus.spring.integration.domain.stages.Nymph;

public interface IncompleteMetamorphosisService {

	Nymph transform(Egg egg);

	Adult transform(Nymph nymph);
}
