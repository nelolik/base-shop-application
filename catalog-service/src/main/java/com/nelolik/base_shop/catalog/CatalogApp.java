package com.nelolik.base_shop.catalog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Hello world!
 *
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CatalogApp
{
    public static void main(String[] args) {
        SpringApplication.run(CatalogApp.class, args);
    }
}
