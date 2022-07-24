package com.nelolik.base_shop.statistic_service.listener;

import com.nelolik.base_shop.statistic_service.service.VisitStatisticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticQueueListener {

    private final VisitStatisticService visitStatisticService;

    @RabbitListener(queues = "${queueName}")
    public void statisticQueueListener(String message) {
        log.info("Received message with text: {}", message);
        visitStatisticService.saveProductPageVisit(message);
    }
}
