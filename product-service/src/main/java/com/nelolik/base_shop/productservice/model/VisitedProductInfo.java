package com.nelolik.base_shop.productservice.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class VisitedProductInfo {

    private Long productId;

    private Long userId;
}
