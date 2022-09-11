package com.nelolik.base_shop.productservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
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
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
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

    private static WireMockServer wireMockServer;

    private static final int port = 8083;

    private static List<ProductShort> shorts;

    private static List<Product> products;

    private static final String category = "category";

    @BeforeAll
    static void beforeAll() {
        shorts = new ArrayList<>();
        for (long i = 1; i < 10; i++) {
            shorts.add(new ProductShort(i, "name" + i, new BigDecimal(i * 100)));
        }

        products = new ArrayList<>();
        LongStream.range(1, 10).forEach(i ->
                products.add(new Product(i, "name" + 1, "description " + i, new BigDecimal(i * 100),
                (int)i, category)));

        wireMockServer = new WireMockServer(port);
        wireMockServer.start();
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
        wireMockServer.stop();
    }

    @Test
    void getProductsForBarTest() throws JsonProcessingException {
        List<Long> recommendedIds = List.of(2L, 4L, 6L);
        List<ProductShort> response = shorts.stream().filter(p -> recommendedIds.contains(p.getId()))
                .collect(Collectors.toList());

        wireMockServer.stubFor(get("/statistic/recommendation")
                .willReturn(aResponse().withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody(new ObjectMapper().writeValueAsString(recommendedIds))));

        when(productMapper.findProductShortsByIds(recommendedIds)).thenReturn(response);

        List<ProductShort> result = productService.getProductsForBar();

        assertThat(result).isNotNull().isEqualTo(response);
        verify(productMapper, never()).getProductsForBar();
        verify(productMapper, times(1)).findProductShortsByIds(recommendedIds);

        reset(productMapper);
        List<ProductShort> result2 = productService.getProductsForBar();

        assertThat(result2).isNotNull().isEqualTo(response);
        verify(productMapper, never()).getProductsForBar();
        verify(productMapper, never()).findProductShortsByIds(anyList());
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
