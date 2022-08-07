package com.nelolik.base_shop.statistic_service.service;

import com.nelolik.base_shop.statistic_service.mapper.ProductStatisticMapper;
import com.nelolik.base_shop.statistic_service.model.ProductStatistic;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class VisitStatisticServiceTest {

    @MockBean
    private ProductStatisticMapper productStatisticMapper;

    @Autowired
    private VisitStatisticService visitStatisticService;


    @Test
    void saveProductPageVisitFirstTimeAddNewRecord() {
        long newId = 10;
        when(productStatisticMapper.getProductStatisticByProductId(newId)).thenReturn(null);
        ProductStatistic newStatistic = new ProductStatistic(newId, 1L, 0L);

        visitStatisticService.saveProductPageVisit(newId);

        verify(productStatisticMapper).saveProductStatistic(newStatistic);
    }

    @Test
    void saveProductPageVisitIncrementsNumberOfViews() {
        long id = 1;
        ProductStatistic existingRecord = new ProductStatistic(id, 20L, 5L);
        ProductStatistic incrementedRecord = new ProductStatistic(id, 21L, 5L);

        when(productStatisticMapper.getProductStatisticByProductId(id)).thenReturn(existingRecord);

        visitStatisticService.saveProductPageVisit(id);

        verify(productStatisticMapper).updateProductStatistic(incrementedRecord);
    }

    @Test
    void getProductStatisticByIdWithPositiveId() {
        long id = 1;
        ProductStatistic existingRecord = new ProductStatistic(id, 20L, 5L);

        when(productStatisticMapper.getProductStatisticByProductId(id)).thenReturn(existingRecord);

        ProductStatistic result = visitStatisticService.getProductStatisticById(id);

        verify(productStatisticMapper).getProductStatisticByProductId(id);
        assertThat(result).isNotNull().isEqualTo(existingRecord);
    }

    @Test
    void getProductStatisticByIdWithNotPositiveId() {
        long id = 0;
        ProductStatistic neverReturned = new ProductStatistic(1L, 1L, 1L);

        when(productStatisticMapper.getProductStatisticByProductId(anyLong())).thenReturn(neverReturned);

        ProductStatistic result = visitStatisticService.getProductStatisticById(id);

        verify(productStatisticMapper, never()).getProductStatisticByProductId(anyLong());
        Assertions.assertNull(result);
    }

    @Test
    void getMostVisitedProductsReturnsListOfIds() {
        List<Long> expected = new ArrayList<>();
        for (long i = 0; i < 10; i++) {
            expected.add(i);
        }

        when(productStatisticMapper.getMostViewedProductIds(10L)).thenReturn(expected);

        List<Long> result = visitStatisticService.getMostVisitedProducts();

        Assertions.assertEquals(expected, result);
    }

}
