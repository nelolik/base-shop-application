package com.nelolik.base_shop.productservice;

import com.nelolik.base_shop.productservice.mapper.ProductMapper;
import com.nelolik.base_shop.productservice.model.Product;
import com.nelolik.base_shop.productservice.model.ProductShort;
import com.nelolik.base_shop.productservice.service.ProductService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.test.context.ContextConfiguration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@SpringBootTest
@EnableRedisRepositories
@ContextConfiguration(classes = RedisTestConfig.class)
public class ProductServiceTest {

    @Autowired
    private RedisServer redisServer;

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductMapper productMapper;

    private static List<ProductShort> shorts;

    private static List<Product> products;

    private static String category = "category";

    @BeforeAll
    static void beforeAll() {
        shorts = new ArrayList<>();
        for (long i = 1; i < 10; i++) {
            shorts.add(new ProductShort(i, "name" + i, new BigDecimal(i * 100)));
        }

        products = new ArrayList<>();
        for (long i = 1; i < 10; i++) {
            products.add(new Product(i, "name" + 1, "description " + i, new BigDecimal(i * 100),
                    (int)i, category));
        }
    }

    @PostConstruct
    void postConstruct() {
        if (!redisServer.isActive()) {
            redisServer.start();
        }
    }

    @PreDestroy
    public void preDestroy() {
        redisServer.stop();
    }

    @Test
    void getProductsForBarTest() {
        when(productMapper.getProductsForBar()).thenReturn(shorts);

        List<ProductShort> result = productService.getProductsForBar();

        assertThat(result).isNotNull().isEqualTo(shorts);
        verify(productMapper, times(1)).getProductsForBar();

        reset(productMapper);
        List<ProductShort> result2 = productService.getProductsForBar();

        assertThat(result2).isNotNull().isEqualTo(shorts);
        verify(productMapper, never()).getProductsForBar();
    }

    @Test
    void getProductByIdSecondTimeShouldUseCache() {
        Product expected = products.get(0);
        when(productMapper.getProductById(expected.getId())).thenReturn(expected);

        Product result = productService.getProductById(expected.getId());

        assertThat(result).isNotNull().isEqualTo(expected);
        verify(productMapper, times(1)).getProductById(expected.getId());

        reset(productMapper);
        Product result2 = productService.getProductById(expected.getId());

        assertThat(result2).isNotNull().isEqualTo(expected);
        verify(productMapper, never()).getProductById(anyLong());
    }

    @Test
    void getProductsByCategoryShouldUseCacheOnSecondCall() {
        List<Product> expected = products;
        when(productMapper.getProductsByCategory(category)).thenReturn(expected);

        List<Product> result = productService.getProductsByCategory(category);

        assertThat(result).isNotNull().isEqualTo(expected);
        verify(productMapper, times(1)).getProductsByCategory(category);

        reset(productMapper);
        List<Product> result2 = productService.getProductsByCategory(category);

        assertThat(result2).isNotNull().isEqualTo(expected);
        verify(productMapper, never()).getProductsByCategory(anyString());
    }
}
