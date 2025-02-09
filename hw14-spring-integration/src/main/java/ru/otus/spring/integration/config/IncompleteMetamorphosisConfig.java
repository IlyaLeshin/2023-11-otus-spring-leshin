package ru.otus.spring.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;

import ru.otus.spring.integration.services.IncompleteMetamorphosisService;


@Configuration
public class IncompleteMetamorphosisConfig {

    @Bean
    public MessageChannelSpec<?, ?> eggsWithIncompleteMetamorphismChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public MessageChannelSpec<?, ?> nymphChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public IntegrationFlow incompleteMetamorphosisEggsFlow(IncompleteMetamorphosisService metamorphosisService) {
        return IntegrationFlow.from(eggsWithIncompleteMetamorphismChannel())
                .split()
                .handle(metamorphosisService, "transform")
                .aggregate()
                .channel("nymphChannel")
                .get();
    }

    @Bean
    public IntegrationFlow nymphFlow(IncompleteMetamorphosisService incompleteMetamorphosisService) {
        return IntegrationFlow.from(nymphChannel())
                .split()
                .handle(incompleteMetamorphosisService, "transform")
                .aggregate()
                .channel("adultChannel")
                .get();
    }
}
