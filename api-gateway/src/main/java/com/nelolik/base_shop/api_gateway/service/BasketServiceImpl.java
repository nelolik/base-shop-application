package com.nelolik.base_shop.api_gateway.service;

import com.nelolik.base_shop.api_gateway.config.ApiUris;
import com.nelolik.base_shop.api_gateway.model.BasketItemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasketServiceImpl implements BasketService {

    private final ApiUris apiUris;


    @Override
    public List<BasketItemDTO> getListOfOrderedProducts(long userId) {
        WebClient client = WebClient.create(apiUris.getBasketListOfProducts() + userId);
        ResponseEntity<List<BasketItemDTO>> responseEntity = client.get().retrieve().toEntityList(BasketItemDTO.class)
                .doOnError(e -> log.error("Error retrieve a list of ordered product from a basket service. \n{}",
                        e.getMessage()))
                .block();
        if (responseEntity != null && responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity.getBody();
        } else {
            return client.get().retrieve().toEntityList(BasketItemDTO.class)
                    .doOnError(e -> log.error(
                            "Error on second retrieve of a list of ordered product from a basket service. \n{}",
                            e.getMessage()))
                    .block().getBody();
        }
    }

    @Override
    public List<BasketItemDTO> addProductToBasket(long userId, long productId, int quantity) {
        WebClient client = WebClient.create(apiUris.getBasketAddProduct());
        ResponseEntity<List<BasketItemDTO>> responseEntity = client.post().uri(uriBuilder -> uriBuilder
                        .queryParam(ATTRIBUTE_USER_ID, userId)
                        .queryParam(ATTRIBUTE_PRODUCT_ID, productId)
                        .queryParam(ATTRIBUTE_QUANTITY, quantity)
                        .build())
                .retrieve().toEntityList(BasketItemDTO.class)
                .doOnError(e -> log.error("Error add product to basket-service. userId={}, productId={}, quantity={}. \n{}",
                        userId, productId, quantity, e.getMessage()))
                .block();
        if (responseEntity != null && responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity.getBody();
        } else {
            return client.post().uri(uriBuilder -> uriBuilder
                    .queryParam(ATTRIBUTE_USER_ID, userId)
                    .queryParam(ATTRIBUTE_PRODUCT_ID, productId)
                    .queryParam(ATTRIBUTE_QUANTITY, quantity)
                    .build())
                    .retrieve().toEntityList(BasketItemDTO.class)
                    .doOnError(e -> log.error(
                            "Error retry to add product to basket-service. userId={}, productId={}, quantity={}. \n{}",
                            userId, productId, quantity, e.getMessage()))
                    .block().getBody();
        }
    }

    @Override
    public List<BasketItemDTO> removeProductFromBasket(long userId, long productId) {
        WebClient client = WebClient.create(apiUris.getBasketRemoveProduct());
        ResponseEntity<List<BasketItemDTO>> responseEntity = client.post()
                .attribute(ATTRIBUTE_USER_ID, userId).attribute(ATTRIBUTE_PRODUCT_ID, productId)
                .retrieve().toEntityList(BasketItemDTO.class)
                .doOnError(e -> log.error("Error remove product from basket. userId={}, productId={} \n{}",
                        userId, productId, e.getMessage())).block();
        if (responseEntity != null && responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity.getBody();
        } else {
            return client.post().attribute(ATTRIBUTE_USER_ID, userId).attribute(ATTRIBUTE_PRODUCT_ID, productId)
                    .retrieve().toEntityList(BasketItemDTO.class)
                    .doOnError(e -> log.error("Error remove product from basket. userId={}, productId={} \n{}",
                            userId, productId, e.getMessage())).block().getBody();
        }
    }

    @Override
    public List<BasketItemDTO> setProductQuantity(long userId, long productId, int quantity) {
        WebClient client = WebClient.create(apiUris.getBasketSetQuantity());
        ResponseEntity<List<BasketItemDTO>> responseEntity = client.post()
                .attribute(ATTRIBUTE_USER_ID, userId)
                .attribute(ATTRIBUTE_PRODUCT_ID, productId)
                .attribute(ATTRIBUTE_QUANTITY, quantity)
                .retrieve().toEntityList(BasketItemDTO.class)
                .doOnError(e -> log.error("Error set product quantity. userId={}, productId={}, quantity={} \n{}",
                        userId, productId, quantity, e.getMessage())).block();
        if (responseEntity != null && responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity.getBody();
        } else {
            return client.post()
                    .attribute(ATTRIBUTE_USER_ID, userId)
                    .attribute(ATTRIBUTE_PRODUCT_ID, productId)
                    .attribute(ATTRIBUTE_QUANTITY, quantity)
                    .retrieve().toEntityList(BasketItemDTO.class)
                    .doOnError(e -> log.error("Error retrying to set product quantity. userId={}, productId={}, quantity={} \n{}",
                            userId, productId, quantity, e.getMessage())).block().getBody();
        }
    }

    @Override
    public void removeBasket(long userId) {
        WebClient client = WebClient.create(apiUris.getBasketRemove());
        ResponseEntity<Void> responseEntity = client.post().attribute(ATTRIBUTE_USER_ID, userId)
                .retrieve().toBodilessEntity()
                .doOnError(e -> log.error("Error remove basket for userId={}. \n{}", userId, e.getMessage()))
                .block();
        if (responseEntity != null && !responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            client.post().attribute(ATTRIBUTE_USER_ID, userId)
                    .retrieve().toBodilessEntity()
                    .doOnError(e -> log.error("Error retrying to remove basket for userId={}. \n{}",
                            userId, e.getMessage()))
                    .block();
        }
    }

    @Override
    public BigDecimal getTotalPrice(long userId) {
        WebClient client = WebClient.create(apiUris.getBasketGetTotalPrice());
        return client.get().attribute(ATTRIBUTE_USER_ID, userId).retrieve()
                .bodyToMono(BigDecimal.class)
                .doOnError(e -> log.error("Error get total price for userId={}, \n{}", userId, e.getMessage()))
                .block();
    }
}
