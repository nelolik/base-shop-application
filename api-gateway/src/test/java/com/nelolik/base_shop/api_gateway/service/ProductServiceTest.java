package com.nelolik.base_shop.api_gateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.nelolik.base_shop.api_gateway.model.Product;
import com.nelolik.base_shop.api_gateway.model.ProductShort;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    private static int port = 8081;
    private static WireMockServer wireMockServer;

    private long ID = 1;
    private Product p1 = new Product(ID++, "first", "description1", 10.0, 15, "category1");
    private Product p2 = new Product(ID++, "second", "description2", 2.5, 23, "category2");
    private Product p3 = new Product(ID++, "third", "description3", 3.4, 45, "category2");

    private List<Product> productList = List.of(p1, p2, p3);
    private List<ProductShort> shorts = productList.stream().map(p -> new ProductShort(p.getId(), p.getName(), p.getPrice()))
            .collect(Collectors.toList());

    @BeforeAll
    static void init() {
        wireMockServer = new WireMockServer(port);
        wireMockServer.start();
    }

    @AfterAll
    static void after() {
        wireMockServer.stop();
    }

    @Test
    void getSearchedProductsTest() throws JsonProcessingException {
        String pattern = "ir";
        List<ProductShort> searchResult = shorts.stream().filter(p -> p.getName().contains(pattern))
                .collect(Collectors.toList());

        wireMockServer.stubFor(get("/products/search/" + pattern)
                .willReturn(aResponse().withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody(new ObjectMapper().writeValueAsString(searchResult))));

        List<ProductShort> result = productService.getSearchedProducts(pattern);
        Assertions.assertEquals(searchResult, result);
    }

    @Test
    void getCategoryProductsShouldReturnTheProductsOfTheGivenCategory() throws JsonProcessingException {
        String category = "category2";
        List<ProductShort> expected = shorts.stream().filter(p -> p.getName().equals(category))
                .collect(Collectors.toList());

        wireMockServer.stubFor(get("/products/category/" + category)
                .willReturn(aResponse().withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody(new ObjectMapper().writeValueAsString(expected))));

        List<ProductShort> result = productService.getCategoryProducts(category);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void getProductByIdShouldReturnSingleProduct() throws JsonProcessingException {
        long id = 1;
        Product expected = p1;

        wireMockServer.stubFor(get("/products/id/" + id)
                .willReturn(aResponse().withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody(new ObjectMapper().writeValueAsString(expected))));

        Product result = productService.getProductById(id);
        Assertions.assertEquals(expected, result);
    }
}
