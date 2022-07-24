package com.nelolik.base_shop.statistic_service;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

@TestConfiguration
@Testcontainers
public class TestConfig {

    @Bean
    public Queue testQueue() {
        return new Queue("statisticQueue", false);
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public RabbitMQContainer queueContainer() {
        return new RabbitMQContainer("rabbitmq:3.10-management")
                .withExposedPorts(5672, 15672)
                .waitingFor(Wait.forListeningPort());
    }

    @Bean
    CachingConnectionFactory cachingConnectionFactory(GenericContainer genericContainer) {
        return new CachingConnectionFactory(genericContainer.getMappedPort(5672));
    }
}
