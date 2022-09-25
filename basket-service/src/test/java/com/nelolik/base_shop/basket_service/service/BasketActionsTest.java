package com.nelolik.base_shop.basket_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.nelolik.base_shop.basket_service.db.mapper.UserBasketDbMapper;
import com.nelolik.base_shop.basket_service.dto.BasketDBO;
import com.nelolik.base_shop.basket_service.model.basket.BasketDb;
import com.nelolik.base_shop.basket_service.dto.BasketItemDTO;
import com.nelolik.base_shop.basket_service.model.product.ProductShort;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BasketActionsTest {

    @Autowired
    private BasketActions basketActions;

    @MockBean
    private BasketDb basketDb;

    @MockBean
    private UserBasketDbMapper userBasketDbMapper;

    private static WireMockServer mockServer;

    private static int productServicePort = 8081;


    private long userId = 1;
    private long basketId = 2;
    private List<BasketDBO> basketDBOS =
            List.of(new BasketDBO(basketId, 3L, "Name 1", BigDecimal.valueOf(12.23), 1),
            new BasketDBO(basketId, 5L, "Name 2", BigDecimal.valueOf(3.48), 2),
            new BasketDBO(basketId, 7L, "Name 3", BigDecimal.valueOf(76.12), 1));
    private List<ProductShort> productShorts = basketDBOS.stream()
            .map(dbo -> new ProductShort(dbo.getProductId(), dbo.getProductName(), dbo.getProductPrice()))
            .collect(Collectors.toList());
    private List<BasketItemDTO> itemDTOS = basketDBOS.stream()
            .map(i -> new BasketItemDTO(
                    new ProductShort(i.getProductId(), i.getProductName(), i.getProductPrice()), i.getQuantity()))
            .collect(Collectors.toList());


    @BeforeAll
    static void beforeAll() {
        mockServer = new WireMockServer(productServicePort);
        mockServer.start();
    }

    @AfterAll
    static void afterAll() {
        mockServer.stop();
    }


    @Test
    void getBasketItemsWithNewBasketCreationTimeTest() {

        when(userBasketDbMapper.getBasketIdForUser(userId)).thenReturn(basketId);
        when(userBasketDbMapper.getCreationTimeByUserId(userId))
                .thenReturn(OffsetDateTime.now(ZoneOffset.from(ZoneOffset.UTC)).minusHours(1L));
        when(basketDb.getBasketItemsById(basketId)).thenReturn(basketDBOS);

        List<BasketItemDTO> result = basketActions.getBasketItems(userId);

        assertThat(result).isNotNull().containsExactlyInAnyOrderElementsOf(itemDTOS);
        Mockito.verify(basketDb, never()).updateBasket(ArgumentMatchers.any(BasketDBO.class));
        Mockito.verify(basketDb, times(1)).getBasketItemsById(basketId);
        Mockito.verify(userBasketDbMapper, times(1)).getBasketIdForUser(1);
        Mockito.verify(userBasketDbMapper, times(1)).getCreationTimeByUserId(userId);
    }

    @Test
    void getBasketItemsWithExpiredBasketCreationTimme() throws JsonProcessingException {
        List<ProductShort> updatedProducts = new ArrayList<>(productShorts);
        for (ProductShort product :
                productShorts) {
            product.setName(product.getName() + " update");
        }
        List<BasketItemDTO> updatedItems = new ArrayList<>();
        List<BasketDBO> updatedDbos = new ArrayList<>();
        for (int i = 0; i < updatedProducts.size(); i++) {
            ProductShort product = updatedProducts.get(i);
            int quantity = itemDTOS.get(i).getQuantity();
            updatedItems.add(new BasketItemDTO(product, quantity));
            updatedDbos.add(new BasketDBO(basketId, product.getId(), product.getName(), product.getPrice(), quantity));
        }
        mockServer.stubFor(get("/products/for_bar/with_ids")
                .willReturn(aResponse().withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody(new ObjectMapper().writeValueAsString(updatedProducts))));

        when(userBasketDbMapper.getBasketIdForUser(userId)).thenReturn(basketId);
        when(userBasketDbMapper.getCreationTimeByUserId(userId))
                .thenReturn(OffsetDateTime.now(ZoneOffset.from(ZoneOffset.UTC)).minusDays(2L));
        when(basketDb.getBasketItemsById(basketId)).thenReturn(basketDBOS, updatedDbos);


        List<BasketItemDTO> result = basketActions.getBasketItems(userId);

        assertThat(result).isNotNull().containsExactlyInAnyOrderElementsOf(updatedItems);
        Mockito.verify(basketDb, times(3)).updateBasket(ArgumentMatchers.any(BasketDBO.class));
        Mockito.verify(basketDb, times(2)).getBasketItemsById(basketId);
        Mockito.verify(userBasketDbMapper, times(1)).getBasketIdForUser(1);
        Mockito.verify(userBasketDbMapper, times(1)).getCreationTimeByUserId(userId);
    }

    @Test
    void addProductToExistingBasketTest() throws JsonProcessingException {
        ProductShort product = productShorts.get(0);
        int quantity = 2;
        when(userBasketDbMapper.getBasketIdForUser(userId)).thenReturn(basketId);
        when(basketDb.getBasketItemsById(basketId)).thenReturn(basketDBOS);
        mockServer.stubFor(get("/products/short/id/" + product.getId())
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(new ObjectMapper().writeValueAsString(product))));


        basketActions.addProductToBasket(product.getId(), quantity, userId);

        Mockito.verify(userBasketDbMapper, times(1)).getBasketIdForUser(userId);
        Mockito.verify(userBasketDbMapper, never()).addNewBasketForUser(anyLong(), any());
        Mockito.verify(basketDb, times(1)).getBasketItemsById(basketId);
        Mockito.verify(basketDb, times(basketDBOS.size())).updateBasket(any());
        Mockito.verify(basketDb, never()).saveBasket(any());
    }

    @Test
    void addProductToNotExistingBasketCreatesNewOne() throws JsonProcessingException {
        ProductShort product = productShorts.get(0);
        int quantity = 2;
        when(userBasketDbMapper.getBasketIdForUser(userId)).thenReturn(null);
        OffsetDateTime creationTime = OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);
        when(userBasketDbMapper.addNewBasketForUser(userId, creationTime)).thenReturn(basketId);
        mockServer.stubFor(get("/products/short/id/" + product.getId())
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(new ObjectMapper().writeValueAsString(product))));

        basketActions.addProductToBasket(product.getId(), quantity, userId);

        Mockito.verify(userBasketDbMapper, times(1)).getBasketIdForUser(userId);
        Mockito.verify(userBasketDbMapper, times(1)).addNewBasketForUser(userId, creationTime);
        Mockito.verify(basketDb, times(1)).getBasketItemsById(basketId);
        Mockito.verify(basketDb, never()).updateBasket(any());
        Mockito.verify(basketDb, times(1)).saveBasket(any());
    }

    @Test
    void removeProductFromExistingBasket() {
        when(userBasketDbMapper.getBasketIdForUser(userId)).thenReturn(basketId);
        when(basketDb.getBasketItemsById(basketId)).thenReturn(basketDBOS);
        Long productId = basketDBOS.get(0).getProductId();

        basketActions.removeProductFromBasket(productId, userId);

        Mockito.verify(userBasketDbMapper, times(1)).getBasketIdForUser(userId);
        Mockito.verify(basketDb, times(1)).getBasketItemsById(basketId);
        Mockito.verify(basketDb, times(1)).deleteItemById(productId, basketId);
    }

    @Test
    void removeProductFromNotExistingBasketShouldRaiseException() {
        when(userBasketDbMapper.getBasketIdForUser(userId)).thenReturn(null);

        Assertions.assertThrows(RuntimeException.class, () -> basketActions.removeProductFromBasket(1L, userId));

        Mockito.verify(userBasketDbMapper, times(1)).getBasketIdForUser(userId);
        Mockito.verify(basketDb, never()).getBasketItemsById(anyLong());
    }

    @Test
    void setProductQuantityTest() {
        BasketDBO basketDBO = basketDBOS.get(0);
        Long productId = basketDBO.getProductId();
        int newQuantity = basketDBO.getQuantity() + 2;
        BasketDBO newDBO = new BasketDBO(basketId, productId, basketDBO.getProductName(), basketDBO.getProductPrice(),
                newQuantity);

        when(userBasketDbMapper.getBasketIdForUser(userId)).thenReturn(basketId);
        when(basketDb.getBasketItemsById(basketId)).thenReturn(basketDBOS);

        basketActions.setProductQuantity(productId, newQuantity, userId);

        Mockito.verify(basketDb, times(1)).getBasketItemsById(basketId);
        Mockito.verify(basketDb, atLeastOnce()).updateBasket(newDBO);
    }

    @Test
    void removeBasketTest() {
        when(userBasketDbMapper.getBasketIdForUser(userId)).thenReturn(basketId);

        basketActions.removeBasket(userId);

        Mockito.verify(basketDb, times(1)).deleteBasket(basketId);
        Mockito.verify(userBasketDbMapper, times(1)).removeBasketByBasketId(basketId);
    }

    @Test
    void getTotalPriceTest() {
        BigDecimal expected = basketDBOS.stream().map(basketDBO ->
                        basketDBO.getProductPrice().multiply(BigDecimal.valueOf(basketDBO.getQuantity())))
                .collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add));
        when(userBasketDbMapper.getBasketIdForUser(userId)).thenReturn(basketId);
        when(userBasketDbMapper.getCreationTimeByUserId(userId)).thenReturn(
                OffsetDateTime.now(ZoneOffset.from(ZoneOffset.UTC)).minusHours(1L));
        when(basketDb.getBasketItemsById(basketId)).thenReturn(basketDBOS);

        BigDecimal totalPrice = basketActions.getTotalPrice(userId);

        Assertions.assertEquals(expected, totalPrice);

        Mockito.verify(userBasketDbMapper, times(1)).getBasketIdForUser(userId);
        Mockito.verify(userBasketDbMapper, times(1)).getCreationTimeByUserId(userId);
        Mockito.verify(basketDb, times(1)).getBasketItemsById(basketId);
    }
}
