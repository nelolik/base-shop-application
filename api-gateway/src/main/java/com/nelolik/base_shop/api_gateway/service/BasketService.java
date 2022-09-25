package com.nelolik.base_shop.api_gateway.service;

import com.nelolik.base_shop.api_gateway.model.BasketItemDTO;

import java.math.BigDecimal;
import java.util.List;

public interface BasketService {

    List<BasketItemDTO> getListOfOrderedProducts(long userId);

    List<BasketItemDTO> addProductToBasket(long userId, long productId, int quantity);

    List<BasketItemDTO> removeProductFromBasket(long userId, long productId);

    List<BasketItemDTO> setProductQuantity(long userId, long productId, int quantity);

    void removeBasket(long userId);

    BigDecimal getTotalPrice(long userId);
}
