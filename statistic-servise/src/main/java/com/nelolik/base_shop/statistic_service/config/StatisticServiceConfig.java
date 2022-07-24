package com.nelolik.base_shop.statistic_service.config;


import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@EnableRabbit
public class StatisticServiceConfig {

    @Value("${queueName}")
    private String queueName;

    @Value("${queueHost")
    private String queueHost;

    @Autowired
    private Environment environment;

    @Bean
    Queue statisticQueue() {
        return new Queue(queueName  , false);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(queueHost);
        return factory;
    }
}
