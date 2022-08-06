package com.nelolik.base_shop.statistic_service.service;

import com.nelolik.base_shop.statistic_service.mapper.ProductStatisticMapper;
import com.nelolik.base_shop.statistic_service.model.ProductStatistic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class VisitStatisticServiceImpl implements VisitStatisticService{

    private final ProductStatisticMapper productMapper;

    @Override
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
    public ProductStatistic getProductStatisticById(long id) {
        if (id <= 0) {
            return null;
        }
        return productMapper.getProductStatisticByProductId(id);
    }

    @Override
    public List<Long> getMostVisitedProducts() {
        long countOfRecommendations = 10; //TODO replace magic number
        return productMapper.getMostViewedProductIds(countOfRecommendations);
    }

}
