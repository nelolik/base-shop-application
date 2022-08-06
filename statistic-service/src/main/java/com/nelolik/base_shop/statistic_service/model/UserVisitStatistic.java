package com.nelolik.base_shop.statistic_service.model;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class UserVisitStatistic {

    private Long userId;

    private Long productId;

    private Long visitCount;
}
