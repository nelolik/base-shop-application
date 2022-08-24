package com.nelolik.base_shop.productservice.model;


import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProductShort implements Serializable {

    private Long id;

    private String  name;

    private BigDecimal price;
}
