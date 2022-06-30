package com.nelolik.productservice;


import com.nelolik.productservice.mapper.ProductMapper;
import com.nelolik.productservice.model.Product;
import com.nelolik.productservice.model.ProductBarElement;
import org.junit.jupiter.api.Assertions;
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
                .extracting(Product::getName).containsExactlyInAnyOrder("toothpaste", "pen", "beacon");
    }

    @Test
    void testGetProductsForBar() {
        List<ProductBarElement> products = productMapper.getProductsForBar();
        assertThat(products).isNotNull().hasSize(3)
                .extracting(ProductBarElement::getName).doesNotContainNull()
                .containsExactlyInAnyOrder("toothpaste", "pen", "beacon");
    }

    @Test
    void testGetProductById() {
        Product expected = new Product(1L, "toothpaste", "gel too clean tooth",
                200., 22, "health");
        Product product = productMapper.getProductById(1L);
        Assertions.assertEquals(expected, product);
    }

    @Test
    void testSaveProduct() {
        Product newProduct = new Product(4L, "new name", "Something very useful",
                1000., 5, "New category");
        productMapper.saveProduct(newProduct);
        List<Product> products = productMapper.getProducts();
        assertThat(products).isNotNull().contains(newProduct);
    }

    @Test
    void testUpdateProduct() {
        List<Product> products = productMapper.getProducts();
        Product product = products.get(0);
        String newName = "Modified name";
        product.setName(newName);

        productMapper.updateProduct(product);
        Product modified = productMapper.getProductById(product.getId());

        Assertions.assertEquals(newName, modified.getName());
    }
}
