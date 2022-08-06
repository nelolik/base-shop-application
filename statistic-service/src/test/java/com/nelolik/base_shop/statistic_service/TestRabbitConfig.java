package com.nelolik.base_shop.statistic_service;


import com.nelolik.base_shop.statistic_service.mapper.ProductStatisticMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.io.Reader;

@TestConfiguration
@Testcontainers
@TestPropertySource("classpath:test.properties")
public class TestRabbitConfig {

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
}
