package com.nelolik.base_shop.productservice.model;


import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProductShort {

    private Long id;

    private String  name;

    private Double price;
}
