package com.nelolik.base_shop.statistic_service.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ProductStatistic {

    private Long id;

    private Long numberOfViews;

    private Long numberOfPurchases;
}
