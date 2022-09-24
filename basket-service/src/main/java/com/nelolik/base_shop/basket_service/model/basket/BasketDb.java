package com.nelolik.base_shop.basket_service.model.basket;

import com.nelolik.base_shop.basket_service.model.dto.BasketDBO;

import java.util.List;

public interface BasketDb {

    List<BasketDBO> getBasketItemsById(long basketId);

    void saveBasket(List<BasketDBO> basketDBOS);

    void updateBasket(BasketDBO basketDBO);

    void deleteItemById(long productId);

    void deleteBasket(long basketId);
}
