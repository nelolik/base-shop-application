package com.nelolik.base_shop.basket_service.model.basket;

import java.util.List;

public interface BasketDb {

    List<BasketDBO> getBasketItemsById(long basketId);

    void saveBasket(List<BasketDBO> basketDBOS);

    void updateBasket(BasketDBO basketDBO);

    void deleteBasket(long basketId);
}
