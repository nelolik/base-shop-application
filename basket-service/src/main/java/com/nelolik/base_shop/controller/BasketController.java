package com.nelolik.base_shop.controller;

import com.nelolik.base_shop.model.ProductShort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/basket")
public class BasketController {

    @GetMapping("/list/{userId}")
    public List<ProductShort> getListOfOrderedProducts() {
        throw new UnsupportedOperationException("Not implemented yet");
        //TODO
    }

    @PostMapping("/add")
    public void addProductToBasket() {
        throw new UnsupportedOperationException("Not implemented yet");
        //TODO arguments id and count
    }
}
