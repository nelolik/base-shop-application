package com.nelolik.productservice;


import com.nelolik.productservice.mapper.ProductMapper;
import com.nelolik.productservice.model.Product;
import com.nelolik.productservice.model.ProductBarElement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@MybatisTest
@Sql(scripts = {"classpath:/dropTable.sql", "classpath:/createTable.sql", "classpath:/fillProductTable.sql"})
public class ProductMapperTest {

    @Autowired
    private ProductMapper productMapper;

    @Test
    void testGetProducts() {
        List<Product> products = productMapper.getProducts();
        assertThat(products).isNotNull().hasSize(3)
                .extracting(p -> p.getName()).containsExactlyInAnyOrder("toothpaste", "pen", "beacon");
    }

    @Test
    void testGetProductsForBar() {
        List<ProductBarElement> products = productMapper.getProductsForBar();
        assertThat(products).isNotNull().hasSize(3)
                .extracting(p -> p.getName()).doesNotContainNull()
                .containsExactlyInAnyOrder("toothpaste", "pen", "beacon");
    }
}
