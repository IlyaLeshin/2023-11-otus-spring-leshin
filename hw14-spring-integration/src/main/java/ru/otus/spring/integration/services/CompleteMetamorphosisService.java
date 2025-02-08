package ru.otus.spring.integration.services;

import ru.otus.spring.integration.domain.stages.Adult;
import ru.otus.spring.integration.domain.stages.Egg;
import ru.otus.spring.integration.domain.stages.Larva;
import ru.otus.spring.integration.domain.stages.Pupa;

public interface CompleteMetamorphosisService {

	Larva transform(Egg egg);

	Pupa transform(Larva larva);

	Adult transform(Pupa pupa);
}
