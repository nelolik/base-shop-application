package com.nelolik.base_shop.basket_service.model.product;


import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Slf4j
public class ProductShort {

    private Long id;

    private String  name;

    private BigDecimal price;

    public static ProductShort requestProductById(long productId) {
        WebClient webClient = WebClient.create("http://localhost:8081/products/short/id/" + productId);
        return webClient.get().retrieve().bodyToMono(ProductShort.class)
                .doOnError(consumer -> log.error(consumer.getMessage())).block();
    }
}
