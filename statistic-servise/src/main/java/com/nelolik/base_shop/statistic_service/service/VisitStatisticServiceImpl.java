package com.nelolik.base_shop.statistic_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelolik.base_shop.statistic_service.model.VisitedProductInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VisitStatisticServiceImpl implements VisitStatisticService{
    @Override
    public void saveProductPageVisit(String productJson) {
        VisitedProductInfo productInfo;
        try {
            productInfo = new ObjectMapper().readValue(productJson, VisitedProductInfo.class);
        } catch (JsonProcessingException e) {
            log.error("Error parsing json string to VisitedProductInfo. Input string: '{}'", productJson);
            throw new RuntimeException(e);
        }

    }
}
