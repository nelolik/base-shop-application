package com.nelolik.base_shop.productservice.model;


import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;


@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {

    private Long id;

    private String  name;

    private String description;

    private BigDecimal price;

    private Integer quantity;

    private String category;

}
