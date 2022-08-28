package com.nelolik.base_shop.productservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelolik.base_shop.productservice.model.Product;
import com.nelolik.base_shop.productservice.model.VisitedProductInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticServiceImpl implements StatisticService {

    private final RabbitTemplate rabbitTemplate;

    private final Queue statisticQueue;

    private final ObjectMapper objectMapper;

    @Override
    public void saveProductVisitWithoutUserInfo(Product product) {
        VisitedProductInfo visitedProductInfo = new VisitedProductInfo(product.getId(), null);
        try {
            String message = objectMapper.writeValueAsString(visitedProductInfo);
            rabbitTemplate.send(statisticQueue.getName(), new Message(message.getBytes()));
        } catch (JsonProcessingException e) {
            log.error(String.format("Error parsing VisitProductInfo with productId=%d and userId=null to json string.",
                    product.getId()));
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveProductVisitWithUserInfo(Product product, long userId) {
        VisitedProductInfo visitedProductInfo = new VisitedProductInfo(product.getId(), userId);
        try {
            String message = objectMapper.writeValueAsString(visitedProductInfo);
            rabbitTemplate.send(statisticQueue.getName(), new Message(message.getBytes()));
        } catch (JsonProcessingException e) {
            log.error(String.format("Error parsing VisitProductInfo with productId=%d and userId=%d to json string.",
                    product.getId(), userId));
            throw new RuntimeException(e);
        }
    }
}
