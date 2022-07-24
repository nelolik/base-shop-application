package com.nelolik.base_shop.statistic_service.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class VisitedProductInfo {

    private Long productId;

    private Long userId;
}
