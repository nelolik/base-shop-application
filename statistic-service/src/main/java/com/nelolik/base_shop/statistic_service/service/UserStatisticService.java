package com.nelolik.base_shop.statistic_service.service;

import java.util.List;

public interface UserStatisticService {

    void saveUserProductPageView(long userId, long productId);

    List<Long> getMostViewedByUserIds(long userId);
}
