package com.nelolik.base_shop.productservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelolik.base_shop.productservice.model.Product;
import com.nelolik.base_shop.productservice.model.VisitedProductInfo;
import com.nelolik.base_shop.productservice.service.StatisticService;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class StatisticServiceTest {

    @Autowired
    private StatisticService statisticService;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    long productId = 1;
    long userId = 2;
    private VisitedProductInfo productInfo = new VisitedProductInfo(productId, userId);
    private VisitedProductInfo productInfoWithNullUser = new VisitedProductInfo(productId, null);
    private Product product = new Product(productInfoWithNullUser.getProductId(), "name1", "description",
            BigDecimal.TEN, 10, "category1");
    @Value("${queue.name.statistic}")
    private String queueName;
    private ObjectMapper jsonMapper = new ObjectMapper();

    @Test
    void saveProductVisitWithoutUserInfoTest() throws JsonProcessingException {
        String message = jsonMapper.writeValueAsString(productInfoWithNullUser);

        statisticService.saveProductVisitWithoutUserInfo(product);

        verify(rabbitTemplate, times(1)).send(queueName, new Message(message.getBytes()));
    }

    @Test
    void saveProductVisitWithUserInfoTest() throws JsonProcessingException {
        String message = jsonMapper.writeValueAsString(productInfo);

        statisticService.saveProductVisitWithUserInfo(product, userId);

        verify(rabbitTemplate, times(1)).send(queueName, new Message(message.getBytes()));
    }
}
