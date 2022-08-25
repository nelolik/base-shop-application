package com.nelolik.base_shop.statistic_service.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelolik.base_shop.statistic_service.model.VisitedProductInfo;
import com.nelolik.base_shop.statistic_service.service.UserStatisticService;
import com.nelolik.base_shop.statistic_service.service.VisitStatisticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticQueueListener {

    private final VisitStatisticService visitStatisticService;

    private final UserStatisticService userStatisticService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "${queueName}")
    public void statisticQueueListener(String message) {
        log.debug("Received message with text: {}", message);
        try {
            objectMapper = new ObjectMapper();
            VisitedProductInfo productInfo = this.objectMapper.readValue(message, VisitedProductInfo.class);
            long productId = productInfo.getProductId();
            long userId = productInfo.getUserId();
            visitStatisticService.saveProductPageVisit(productId);
            userStatisticService.saveUserProductPageView(productId, userId);
        } catch (JsonProcessingException e) {
            log.error("Error parsing json string to VisitedProductInfo. Input string: '{}'", message);
            throw new RuntimeException(e);
        }
    }
}
