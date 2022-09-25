package com.nelolik.base_shop.api_gateway.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nelolik.base_shop.api_gateway.jackson.BigDecimalMoneyDeserializer;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ProductShort {

    private Long id;

    private String  name;

    @JsonDeserialize(using = BigDecimalMoneyDeserializer.class)
    private BigDecimal price;
}
