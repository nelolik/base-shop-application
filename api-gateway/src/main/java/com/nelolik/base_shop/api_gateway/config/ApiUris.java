package com.nelolik.base_shop.api_gateway.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "uri")
@Getter
@Setter
public class ApiUris {

    private String catalog;
    private String productCategory;
    private String productId;
    private String productSearch;
    private String productsForBar;
}
