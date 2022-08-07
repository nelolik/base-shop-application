package com.nelolik.base_shop.statistic_service.controller;


import com.nelolik.base_shop.statistic_service.model.ProductStatistic;
import com.nelolik.base_shop.statistic_service.service.UserStatisticService;
import com.nelolik.base_shop.statistic_service.service.VisitStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistic")
@RequiredArgsConstructor
public class StatisticInfoController {

    private final VisitStatisticService visitStatisticService;

    private final UserStatisticService userStatisticService;

    @GetMapping("/product/{id}")
    ProductStatistic getProductStatisticById(@PathVariable("id") long id) {
        return visitStatisticService.getProductStatisticById(id);
    }

    @GetMapping("/recommendation")
    List<Long> getRecommendedProductIds() {
        return visitStatisticService.getMostVisitedProducts();
    }

    @GetMapping("/user_recommendation/{id}")
    List<Long> getRecommendedForUserProductIds(@PathVariable("id") long userId) {
        return userStatisticService.getMostViewedByUserIds(userId);
    }
}
