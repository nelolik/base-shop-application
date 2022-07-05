package com.nelolik.base_shop.api_gateway.controller;

import com.nelolik.base_shop.api_gateway.model.Product;
import com.nelolik.base_shop.api_gateway.model.ProductShort;
import com.nelolik.base_shop.api_gateway.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MainPageController {

    private final ProductService productService;

    @GetMapping("/catalog/names")
    public List<String> getCatalogItemNames() {
        return null;//TODO
    }

    @GetMapping("/advices")
    public List<ProductShort> getPopularItems() {
        return productService.getPopularProductsForBar();
    }

    @GetMapping("/catalog/category/{name}")
    public List<ProductShort> getCategoryItems(@PathVariable("name") String name) {
        return productService.getCategoryProducts(name);
    }

    @GetMapping("/catalog/id/{id}")
    public Product getItemById(@PathVariable("id") long id) {
        return productService.getProductById(id);
    }

}
