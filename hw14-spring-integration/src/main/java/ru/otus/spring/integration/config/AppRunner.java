package ru.otus.spring.integration.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.spring.integration.services.EggIncubationService;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {
	private final EggIncubationService eggsService;

	@Override
	public void run(String... args) {
		eggsService.startGenerateEggsLoop();
	}
}
