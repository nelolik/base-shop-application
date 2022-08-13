package com.nelolik.base_shop.productservice.model;


import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProductShort {

    private Long id;

    private String  name;

    private BigDecimal price;
}
