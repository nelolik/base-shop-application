package com.nelolik.base_shop.basket_service.model.basket;

import com.nelolik.base_shop.basket_service.model.product.ProductShort;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BasketItemDTO {

    private ProductShort product;

    private int quantity;
}
