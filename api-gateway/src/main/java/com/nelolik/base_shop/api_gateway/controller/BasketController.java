package com.nelolik.base_shop.api_gateway.controller;


import com.nelolik.base_shop.api_gateway.model.BasketItemDTO;
import com.nelolik.base_shop.api_gateway.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController("/basket")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @GetMapping("/list/{userId}")
    public List<BasketItemDTO> getListOfOrderedProducts(@PathVariable("userId") long userId) {
        return basketService.getListOfOrderedProducts(userId);
    }

    @PostMapping("/add")
    public List<BasketItemDTO> addProductToBasket(@RequestParam("user_id") long userId,
                                   @RequestParam("product_id") long productId,
                                   @RequestParam("quantity") int quantity) {
        return basketService.addProductToBasket(userId, productId, quantity);
    }

    @PostMapping("/remove_product")
    public List<BasketItemDTO> removeProductFromBasket(@RequestParam("user_id") long userId,
                                                       @RequestParam("product_id") long productId) {
        return basketService.removeProductFromBasket(userId, productId);
    }

    @PostMapping("/set_quantity")
    public List<BasketItemDTO> setProductQuantity(@RequestParam("user_id") long userId,
                                               @RequestParam("product_id") long productId,
                                               @RequestParam("quantity") int quantity) {
        return basketService.setProductQuantity(userId, productId, quantity);
    }

    @PostMapping("/remove_basket")
    public void removeBasket(@RequestParam("userId") long userId) {
        basketService.removeBasket(userId);
    }

    @GetMapping("/total_price")
    public BigDecimal getTotalPrice(@RequestParam("userId") long userId) {
        return basketService.getTotalPrice(userId);
    }

}
