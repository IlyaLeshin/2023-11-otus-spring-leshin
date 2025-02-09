package ru.otus.hw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Application.class);

        SpringApplication.run(Application.class, args);

        logger.info("http://localhost:8080/ - ссылка для быстрого перехода к проверке приложения");
        logger.info("http://localhost:8080/actuator/metrics - ссылка для быстрого перехода к метрикам");
        logger.info("http://localhost:8080/actuator/health - ссылка для быстрого перехода к healthcheck индикатору");
        logger.info("http://localhost:8080/actuator/logfile - ссылка для быстрого перехода к logfile");
    }

}
