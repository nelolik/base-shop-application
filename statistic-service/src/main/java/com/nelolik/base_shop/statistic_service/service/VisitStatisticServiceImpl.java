package com.nelolik.base_shop.statistic_service.service;

import com.nelolik.base_shop.statistic_service.config.CacheNames;
import com.nelolik.base_shop.statistic_service.mapper.ProductStatisticMapper;
import com.nelolik.base_shop.statistic_service.model.ProductStatistic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class VisitStatisticServiceImpl implements VisitStatisticService{

    private final ProductStatisticMapper productMapper;

    @Override
//    @CacheEvict(value = CacheNames.PRODUCT_STATISTIC, key = "#productIds")
    public void saveProductPageVisit(long productId) {
        ProductStatistic previousProductRecord = productMapper.getProductStatisticByProductId(productId);
        if (previousProductRecord != null) {
            previousProductRecord.setNumberOfViews(previousProductRecord.getNumberOfViews() + 1);
            productMapper.updateProductStatistic(previousProductRecord);
        } else {
            productMapper.saveProductStatistic(new ProductStatistic(productId, 1L, 0L));
        }
    }

    @Override
    @Cacheable(value = CacheNames.PRODUCT_STATISTIC, key = "#id")
    public ProductStatistic getProductStatisticById(long id) {
        if (id <= 0) {
            return null;
        }
        return productMapper.getProductStatisticByProductId(id);
    }

    @Override
    @Cacheable(CacheNames.MOST_VISITED)
    public List<Long> getMostVisitedProducts() {
        long countOfRecommendations = 10; //TODO replace magic number
        return productMapper.getMostViewedProductIds(countOfRecommendations);
    }

}
