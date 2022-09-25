package com.nelolik.base_shop.basket_service.dto;

import com.nelolik.base_shop.basket_service.model.product.ProductShort;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class BasketItemDTO {

    private ProductShort product;

    private int quantity;
}
