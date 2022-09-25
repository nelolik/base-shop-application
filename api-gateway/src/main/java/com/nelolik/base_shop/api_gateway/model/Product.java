package com.nelolik.base_shop.api_gateway.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nelolik.base_shop.api_gateway.jackson.BigDecimalMoneyDeserializer;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private Long id;

    private String  name;

    private String description;

    @JsonDeserialize(using = BigDecimalMoneyDeserializer.class)
    private BigDecimal price;

    private Integer quantity;

    private String category;

}
