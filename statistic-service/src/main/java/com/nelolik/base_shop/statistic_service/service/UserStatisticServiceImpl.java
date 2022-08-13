package com.nelolik.base_shop.statistic_service.service;

import com.nelolik.base_shop.statistic_service.config.CacheNames;
import com.nelolik.base_shop.statistic_service.mapper.UserVisitStatisticMapper;
import com.nelolik.base_shop.statistic_service.model.UserVisitStatistic;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserStatisticServiceImpl implements UserStatisticService {

    private final UserVisitStatisticMapper userMapper;

    @Override
    public void saveUserProductPageView(long userId, long productId) {
        UserVisitStatistic prevUserRecord = userMapper.getUserProductStatistic(userId, productId);
        if (prevUserRecord != null) {
            prevUserRecord.setVisitCount(prevUserRecord.getVisitCount() + 1);
            userMapper.updateUserProductVisitCount(prevUserRecord);
        } else {
            userMapper.saveNewProductVisit(new UserVisitStatistic(userId, productId, 1L));
        }
    }

    @Override
    @Cacheable(value = CacheNames.MOST_VIEWED_BY_USER, key = "#userId")
    public List<Long> getMostViewedByUserIds(long userId) {
        int count = 10; //TODO remove magic number
        return userMapper.getProductsMostOftenVisitedByUser(userId, count);
    }
}
