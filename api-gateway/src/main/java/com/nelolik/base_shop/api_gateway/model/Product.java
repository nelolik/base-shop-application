package com.nelolik.base_shop.api_gateway.model;


import lombok.*;


@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private Long id;

    private String  name;

    private String description;

    private Double price;

    private Integer quantity;

    private String category;

}
