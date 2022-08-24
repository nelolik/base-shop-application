package com.nelolik.base_shop.api_gateway.controller;


import com.nelolik.base_shop.api_gateway.model.ProductShort;
import com.nelolik.base_shop.api_gateway.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final ProductService productService;

    @GetMapping("/{text}")
    public List<ProductShort> getSearchedItems(@PathVariable("text") String searchText) {
        return productService.getSearchedProducts(searchText);
    }

}
