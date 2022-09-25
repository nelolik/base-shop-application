package com.nelolik.base_shop.api_gateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.nelolik.base_shop.api_gateway.model.BasketItemDTO;
import com.nelolik.base_shop.api_gateway.model.ProductShort;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BasketServiceTest {

    @Autowired
    private BasketService basketService;


    private static final int port = 8084;
    private static WireMockServer mockServer;

    private long ID = 1;
    private ProductShort p1 = new ProductShort(ID++, "first", BigDecimal.valueOf(1050L, 2));
    private ProductShort p2 = new ProductShort(ID++, "second", BigDecimal.valueOf(250L, 2));
    private ProductShort p3 = new ProductShort(ID++, "third", BigDecimal.valueOf(340L, 2));

    private BasketItemDTO i1 = new BasketItemDTO(p1, 1);
    private BasketItemDTO i2 = new BasketItemDTO(p2, 3);
    private BasketItemDTO i3 = new BasketItemDTO(p3, 2);

    private List<BasketItemDTO> items = List.of(i1, i2, i3);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void beforeAll() {
        mockServer = new WireMockServer(port);
        mockServer.start();
    }

    @AfterAll
    static void afterAll() {
        mockServer.stop();
    }

    @Test
    void getListOfOrderedProductsTest() throws JsonProcessingException {
        long userId = 15;

        String body = objectMapper.writeValueAsString(items);
        mockServer.stubFor(get("/basket/list/" + userId)
                .willReturn(aResponse().withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(body)));

        List<BasketItemDTO> result = basketService.getListOfOrderedProducts(userId);

        assertThat(result).isNotNull().containsExactlyElementsOf(items);
    }

    @Test
    void addProductToBasketTest() throws JsonProcessingException {
        //when
        long userId = 15;
        long productId = ID;
        int quantity = 3;
        ProductShort newProduct = new ProductShort(productId, "new product", BigDecimal.valueOf(1345L, 2));
        List<BasketItemDTO> updatedBasket = new ArrayList<>(items);
        updatedBasket.add(new BasketItemDTO(newProduct, quantity));

        mockServer.stubFor(post("/basket/add")
                .withQueryParam(BasketService.ATTRIBUTE_USER_ID, equalToIgnoreCase(String.valueOf(userId)))
                .withQueryParam(BasketService.ATTRIBUTE_PRODUCT_ID, equalToIgnoreCase(String.valueOf(productId)))
                .withQueryParam(BasketService.ATTRIBUTE_QUANTITY, equalToIgnoreCase(String.valueOf(quantity)))
                .willReturn(aResponse().withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(updatedBasket))));

        //then
        List<BasketItemDTO> result = basketService.addProductToBasket(userId, productId, quantity);

        //verify
        assertThat(result).isNotNull().containsExactlyElementsOf(updatedBasket);
    }
}
