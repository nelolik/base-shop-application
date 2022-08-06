package com.nelolik.base_shop.statistic_service.service;

import com.nelolik.base_shop.statistic_service.mapper.UserVisitStatisticMapper;
import com.nelolik.base_shop.statistic_service.model.UserVisitStatistic;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserStatisticServiceTest {

    @MockBean
    private UserVisitStatisticMapper userVisitStatisticMapper;

    @Autowired
    private UserStatisticService userStatisticService;

    @Test
    void saveUserProductPageViewWithNewProductIdOrUserIdAndNewRecord() {
        long userId = 1L;
        long productId = 2L;
        UserVisitStatistic newRecord = new UserVisitStatistic(userId, productId, 1L);

        when(userVisitStatisticMapper.getUserProductStatistic(userId, productId)).thenReturn(null);

        userStatisticService.saveUserProductPageView(userId, productId);

        verify(userVisitStatisticMapper, atLeast(1)).saveNewProductVisit(newRecord);
    }

    @Test
    void saveUserProductPageViewWithExistingProductIdAndUserIdIncrementsVisitCount() {
        long userId = 1L;
        long productId = 2L;
        UserVisitStatistic existing = new UserVisitStatistic(userId, productId, 1L);
        UserVisitStatistic updated = new UserVisitStatistic(userId, productId, 2L);

        when(userVisitStatisticMapper.getUserProductStatistic(userId, productId)).thenReturn(existing);

        userStatisticService.saveUserProductPageView(userId, productId);

        verify(userVisitStatisticMapper).updateUserProductVisitCount(updated);
    }

    @Test
    void getMostViewedByUserIdsReturnsListOfIds() {
        long userId = 1;
        List<Long> expected = List.of(6L, 1L, 3L, 2L, 5L, 4L);

        when(userVisitStatisticMapper.getProductsMostOftenVisitedByUser(userId, 10)).thenReturn(expected);

        List<Long> result = userStatisticService.getMostViewedByUserIds(userId);

        verify(userVisitStatisticMapper, atLeastOnce()).getProductsMostOftenVisitedByUser(userId, 10);
        assertThat(result).isNotNull().containsExactlyElementsOf(expected);
    }
}
