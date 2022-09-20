package com.nelolik.base_shop.basket_service.service;

import com.nelolik.base_shop.basket_service.model.basket.Basket;
import com.nelolik.base_shop.basket_service.model.basket.BasketDb;
import com.nelolik.base_shop.basket_service.model.basket.BasketItemDTO;
import com.nelolik.base_shop.basket_service.model.product.ProductShort;
import com.nelolik.base_shop.basket_service.db.mapper.UserBasketDbMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class BasketActionsImpl implements BasketActions {

    private final UserBasketDbMapper userBasketDb;

    private final BasketDb basketDb;

    private static final ZoneOffset DEFAULT_OFFSET = ZoneOffset.from(ZoneOffset.UTC);

    @Override
    public List<BasketItemDTO> getBasketItems(long userId) {
        Long basketId = userBasketDb.getBasketIdForUser(userId);
        if (basketId == null) {
            return null;
        }
        OffsetDateTime creationTime = userBasketDb.getCreationTimeByUserId(userId);
        if (creationTime.plusDays(1).isBefore(OffsetDateTime.now(DEFAULT_OFFSET))) {
            validateProducts(basketId);
        }
        return Basket.getBasketById(basketDb, basketId).getItemsList().stream()
                .map(i -> new BasketItemDTO(i.getProduct(), i.getQuantity())).collect(Collectors.toList());
    }

    private void validateProducts(Long basketId) {
        Basket basket = Basket.getBasketById(basketDb, basketId);
        List<ProductShort> productList = basket.getProductList();
        String ids = productList.stream().map(ProductShort::getId).map(Object::toString)
                .collect(Collectors.joining(","));
        WebClient webClient = WebClient.create("http://localhost:8081/products/for_bar/with_ids");
        List<ProductShort> products = webClient.get().attribute("ids", ids)
                .retrieve().bodyToFlux(ProductShort.class).collectList().block();
        if (products == null) {
            log.error("Error getting ProductShort list for validating. " +
                    "http request returned null.");
            return;
        }
        boolean changed = false;
        for (ProductShort product:
             productList) {
            Long id = product.getId();
            Optional<ProductShort> productOptional = products.stream().filter(p -> p.getId().equals(id)).findFirst();
            if (productOptional.isEmpty()) {
                log.error("Error getting ProductShort list for validating. " +
                        "Not all elements of the origin list are present in the received one.");
                return;
            }
            ProductShort productActual = productOptional.get();
            if (!product.equals(productActual)) {
                product.setName(productActual.getName());
                product.setPrice(productActual.getPrice());
                changed = true;
            }
        }
        if (changed) {
            basket.updateBasket(basketDb);
        }
    }

    @Override
    public void addProductToBasket(long productId, int quantity, long userId) {
        Long basketId = userBasketDb.getBasketIdForUser(userId);
        boolean newBasket = false;
        if (basketId == null) {
            OffsetDateTime now = OffsetDateTime.now(DEFAULT_OFFSET);
            basketId = userBasketDb.addNewBasketForUser(userId, now);
            if (basketId == null) {
                log.error("Unable create new basket record in user_basket db for user with id=" + userId);
                throw new RuntimeException("Unable create new basket record in user_basket db for user with id="
                                            + userId);
            }
            newBasket = true;
        }
        ProductShort product = ProductShort.requestProductById(productId);
        if (product == null) {
            log.error(String.format(
                    "Trying to add a product with id=%d. Product with this id does not exist or product-service does not answer",
                    productId));
            return;
        }
        Basket basket = Basket.getBasketById(basketDb, basketId);
        basket.addProduct(product, quantity);
        if (newBasket) {
            basket.saveBasket(basketDb);
        } else {
            basket.updateBasket(basketDb);
        }
    }

    @Override
    public void removeProductFromBasket(long productId, long userId) {
        Long basketId = userBasketDb.getBasketIdForUser(userId);
        if (basketId == null) {
            throw new RuntimeException(
                    String.format("Could not find any basket for userId=%d while attempting to remove thr product with id=%d",
                            userId, productId));
        }
        Basket basket = Basket.getBasketById(basketDb, basketId);
        basket.removeProductFromBasket(productId);
        basket.saveBasket(basketDb);
    }

    @Override
    public void setProductQuantity(long productId, int quantity, long userId) {
        Long basketId = userBasketDb.getBasketIdForUser(userId);
        if (basketId == null) {
            log.error("Could not find any basket for userId={} while attempting to set quantity of the product with id={}",
                    userId, productId);
            throw new RuntimeException(
                    String.format("Could not find any basket for userId=%d while attempting to set quantity of the product with id=%d",
                            userId, productId));
        }
        Basket basket = Basket.getBasketById(basketDb, basketId);
        basket.setProductQuantity(productId, quantity);
        basket.saveBasket(basketDb);
    }

    @Override
    @Transactional
    public void removeBasket(long userId) {
        Long basketId = userBasketDb.getBasketIdForUser(userId);
        if (basketId == null) {
            log.error("Could not find any basket for userId={} while attempting to remove", userId);
            return;
        }
        Basket.removeBasket(basketDb, basketId);
        userBasketDb.removeBasketByBasketId(basketId);
    }

    @Override
    public BigDecimal getTotalPrice(long userId) {
        Long basketId = userBasketDb.getBasketIdForUser(userId);
        OffsetDateTime creationTime = userBasketDb.getCreationTimeByUserId(userId);
        if (creationTime.plusDays(1).isBefore(OffsetDateTime.now(DEFAULT_OFFSET))) {
            validateProducts(basketId);
        }
        Basket basket = Basket.getBasketById(basketDb, basketId);
        return basket.getTotalPrice();
    }
}
