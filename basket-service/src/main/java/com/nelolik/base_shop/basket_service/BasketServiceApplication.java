package com.nelolik.base_shop.basket_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BasketServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BasketServiceApplication.class, args);
    }
}