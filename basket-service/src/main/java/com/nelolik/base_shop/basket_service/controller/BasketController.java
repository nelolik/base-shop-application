package com.nelolik.base_shop.basket_service.controller;

import com.nelolik.base_shop.basket_service.model.basket.BasketItemDTO;
import com.nelolik.base_shop.basket_service.service.BasketActions;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/basket")
@RequiredArgsConstructor
public class BasketController {

    private final BasketActions basketActions;

    @GetMapping("/list/{userId}")
    public List<BasketItemDTO> getListOfOrderedProducts(@PathVariable("userId") long userId) {
        return basketActions.getBasketItems(userId);
    }

    @PostMapping("/add")
    public List<BasketItemDTO> addProductToBasket(@RequestParam("user_id") long userId,
                                   @RequestParam("product_id") long productId,
                                   @RequestParam("quantity") int quantity) {
        basketActions.addProductToBasket(productId, quantity, userId);
        return basketActions.getBasketItems(userId);
    }

    @PostMapping("/remove_item")
    public List<BasketItemDTO> removeProductFromBasket(@RequestParam("user_id") long userId,
                                                       @RequestParam("product_id") long productId) {
        basketActions.removeProductFromBasket(productId, userId);
        return basketActions.getBasketItems(userId);
    }

    @PostMapping("/set_quantity")
    public List<BasketItemDTO> setItemQuantity(@RequestParam("user_id") long userId,
                                               @RequestParam("product_id") long productId,
                                               @RequestParam("quantity") int quantity) {
        basketActions.setProductQuantity(productId, quantity, userId);
        return basketActions.getBasketItems(userId);
    }

    @PostMapping("/remove_basket")
    public void removeBasket(@RequestParam("userId") long userId) {
        basketActions.removeBasket(userId);
    }

    @GetMapping("/total_price")
    public BigDecimal getTotalPrice(@RequestParam("userId") long userId) {
        return basketActions.getTotalPrice(userId);
    }
}
