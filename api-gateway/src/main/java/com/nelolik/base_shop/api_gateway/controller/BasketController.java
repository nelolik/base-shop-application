package com.nelolik.base_shop.api_gateway.controller;


import com.nelolik.base_shop.api_gateway.model.ProductShort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/basket")
public class BasketController {

    @GetMapping("/list")
    public List<ProductShort> getListOfOrderedProducts() {
        return null; //TODO
    }

    @PostMapping("/add")
    public void addProductToBasket() {
        //TODO arguments id and count
    }

}
