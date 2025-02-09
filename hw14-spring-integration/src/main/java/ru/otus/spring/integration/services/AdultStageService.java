package ru.otus.spring.integration.services;

import ru.otus.spring.integration.domain.stages.Adult;

public interface AdultStageService {

	void releaseIntoWild(Adult adult);
}
