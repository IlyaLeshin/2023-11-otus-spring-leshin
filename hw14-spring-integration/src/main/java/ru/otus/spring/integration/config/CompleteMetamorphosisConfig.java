package ru.otus.spring.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import ru.otus.spring.integration.services.CompleteMetamorphosisService;

@Configuration
public class CompleteMetamorphosisConfig {

    @Bean
    public MessageChannelSpec<?, ?> eggsWithCompleteMetamorphismChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public MessageChannelSpec<?, ?> larvaChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public MessageChannelSpec<?, ?> pupaChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public IntegrationFlow completeMetamorphosisEggsFlow(CompleteMetamorphosisService metamorphosisService) {
        return IntegrationFlow.from(eggsWithCompleteMetamorphismChannel())
                .split()
                .handle(metamorphosisService, "transform")
                .aggregate()
                .channel("larvaChannel")
                .get();
    }

    @Bean
    public IntegrationFlow larvaFlow(CompleteMetamorphosisService completeMetamorphosisService) {
        return IntegrationFlow.from(larvaChannel())
                .split()
                .handle(completeMetamorphosisService, "transform")
                .aggregate()
                .channel("pupaChannel")
                .get();
    }

    @Bean
    public IntegrationFlow pupaFlow(CompleteMetamorphosisService completeMetamorphosisService) {
        return IntegrationFlow.from(pupaChannel())
                .split()
                .handle(completeMetamorphosisService, "transform")
                .aggregate()
                .channel("adultChannel")
                .get();
    }
}
