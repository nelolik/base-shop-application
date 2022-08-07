package com.nelolik.base_shop.statistic_service.queue_listener;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelolik.base_shop.statistic_service.listener.StatisticQueueListener;
import com.nelolik.base_shop.statistic_service.model.VisitedProductInfo;
import com.nelolik.base_shop.statistic_service.service.VisitStatisticService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;


@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = TestRabbitConfig.class)
public class QueueListenerTest {

    @Autowired
    private StatisticQueueListener listener;

    @MockBean
    private VisitStatisticService statisticService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static VisitedProductInfo productInfo;
    private static String message;

    @BeforeAll
    static void init() throws JsonProcessingException {
        productInfo = new VisitedProductInfo(1L, 2L);
        message = new ObjectMapper().writeValueAsString(productInfo);
    }

    @Test
    void listenerReceiveMessageFromTargetQueue() {

        rabbitTemplate.send("statisticQueue", new Message(message.getBytes()));

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
                verify(statisticService).saveProductPageVisit(productInfo.getProductId()));
    }

    @Test
    void listenerShouldNotReceiveMessageFromOtherQueues() {

        rabbitTemplate.send("otherQueue", new Message(message.getBytes()));

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() ->
                verify(statisticService, never()).saveProductPageVisit(anyLong()));
    }
}
