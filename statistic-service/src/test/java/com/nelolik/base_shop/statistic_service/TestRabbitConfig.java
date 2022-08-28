package com.nelolik.base_shop.statistic_service;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Testcontainers;


@TestConfiguration
@Testcontainers
@TestPropertySource("classpath:test.properties")
public class TestRabbitConfig {

    public static final int DEFAULT_AMQPS_PORT = 5671;

    public static final int DEFAULT_AMQP_PORT = 5672;

    public static final int DEFAULT_HTTPS_PORT = 15671;

    public static final int DEFAULT_HTTP_PORT = 15672;

    @Bean
    public CachingConnectionFactory connectionFactoryTest(RabbitMQContainer queueContainer) {
        return new CachingConnectionFactory(queueContainer.getHost(),
                queueContainer.getMappedPort(DEFAULT_AMQP_PORT));
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public RabbitMQContainer queueContainer(Queue queue) {
        return new RabbitMQContainer("rabbitmq:3.10-management")
                .withQueue(queue.getName())
                .withExposedPorts(DEFAULT_AMQPS_PORT, DEFAULT_AMQP_PORT,
                        DEFAULT_HTTPS_PORT, DEFAULT_HTTP_PORT);
    }
}
