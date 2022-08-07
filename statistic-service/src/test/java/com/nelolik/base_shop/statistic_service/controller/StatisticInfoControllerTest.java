package com.nelolik.base_shop.statistic_service.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelolik.base_shop.statistic_service.model.ProductStatistic;
import com.nelolik.base_shop.statistic_service.service.UserStatisticService;
import com.nelolik.base_shop.statistic_service.service.VisitStatisticService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class StatisticInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VisitStatisticService visitStatisticService;

    @MockBean
    private UserStatisticService userStatisticService;

    @Test
    void getProductStatisticByIdTest() throws Exception {
        long productId = 1;
        ProductStatistic expected = new ProductStatistic(productId, 10L, 5L);
        String expectedJson = new ObjectMapper().writeValueAsString(expected);

        when(visitStatisticService.getProductStatisticById(productId)).thenReturn(expected);
        mockMvc.perform(get("/statistic/product/" + productId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(expectedJson));

        verify(visitStatisticService, atLeastOnce()).getProductStatisticById(productId);
    }

    @Test
    void getRecommendedProductIdsTest() throws Exception {
        List<Long> recommendedIds = List.of(1L, 2L, 3L, 4L);

        when(visitStatisticService.getMostVisitedProducts()).thenReturn(recommendedIds);

        mockMvc.perform(get("/statistic/recommendation"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(recommendedIds)));

        verify(visitStatisticService, atLeastOnce()).getMostVisitedProducts();
    }

    @Test
    void getRecommendedForUserProductIdsTest() throws Exception {
        long userId = 1L;
        List<Long> recommendedIds = List.of(1L, 2L, 3L, 4L);

        when(userStatisticService.getMostViewedByUserIds(userId)).thenReturn(recommendedIds);

        mockMvc.perform(get("/statistic/user_recommendation/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(recommendedIds)));

        verify(userStatisticService, atLeastOnce()).getMostViewedByUserIds(userId);
    }

}
