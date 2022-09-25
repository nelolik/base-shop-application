package com.nelolik.base_shop.basket_service.service;

import com.nelolik.base_shop.basket_service.dto.BasketItemDTO;

import java.math.BigDecimal;
import java.util.List;

public interface BasketActions {

    List<BasketItemDTO> getBasketItems(long userId);

    void addProductToBasket(long productId, int quantity, long userId);

    void removeProductFromBasket(long productId, long userId);

    void setProductQuantity(long productId, int quantity, long userId);

    void removeBasket(long userId);

    BigDecimal getTotalPrice(long userId);

}
