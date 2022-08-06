package com.nelolik.base_shop.statistic_service.service;

import com.nelolik.base_shop.statistic_service.model.ProductStatistic;

import java.util.List;

public interface VisitStatisticService {

    void saveProductPageVisit(long productId);

    ProductStatistic getProductStatisticById(long id);

    List<Long> getMostVisitedProducts();

}
