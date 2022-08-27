package com.nelolik.base_shop.productservice.service;

import com.nelolik.base_shop.productservice.model.Product;

public interface StatisticService {

    public void saveProductVisitWithoutUserInfo(Product product);

    public void saveProductVisitWithUserInfo(Product product, long userId);
}
