package com.nelolik.base_shop.basket_service.model.basket;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class BasketDBO {

    private Long basketId;

    private Long productId;

    private String  productName;

    private BigDecimal productPrice;

    private int quantity;
}
