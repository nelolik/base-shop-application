package com.nelolik.base_shop.api_gateway.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class BasketItemDTO {

    private ProductShort product;

    private int quantity;
}
