package ru.otus.spring.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;

import ru.otus.spring.integration.domain.MetamorphosisType;
import ru.otus.spring.integration.domain.stages.Egg;
import ru.otus.spring.integration.services.AdultStageService;

@Configuration
public class IntegrationConfig {

    @Bean
    public MessageChannelSpec<?, ?> eggsChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public MessageChannelSpec<?, ?> adultChannel() {
        return MessageChannels.publishSubscribe();
    }


    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(2);
    }

    @Router
    public String eggDirectionChanel(Egg egg) {
        return (egg.type().metamorphosisType()).equals(MetamorphosisType.COMPLETE)
                ? "eggsWithCompleteMetamorphismChannel" : "eggsWithIncompleteMetamorphismChannel";
    }

    @Bean
    public IntegrationFlow eggsFlow() {
        return IntegrationFlow.from(eggsChannel())
                .route(this,"eggDirectionChanel")
                .get();
    }

    @Bean
    public IntegrationFlow adultFlow(AdultStageService adultStageService) {
        return IntegrationFlow.from(adultChannel())
                .split()
                .handle(adultStageService, "releaseIntoWild")
                .get();
    }

}
