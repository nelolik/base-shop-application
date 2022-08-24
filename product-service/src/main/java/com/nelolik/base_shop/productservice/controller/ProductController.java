package com.nelolik.base_shop.productservice.controller;

import com.nelolik.base_shop.productservice.model.ProductShort;
import com.nelolik.base_shop.productservice.service.ProductService;
import com.nelolik.base_shop.productservice.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping()
    public List<Product> getProducts() {
        List<Product> products = productService.getProducts();
        if (products == null) {
            return Collections.emptyList();
        }
        return products;
    }

    @GetMapping("/for_bar")
    public List<ProductShort> getProductListForBar() {
        List<ProductShort> products = productService.getProductsForBar();
        if (products == null) {
            products = Collections.emptyList();
        }
        return products;
    }

    @GetMapping("/id/{id}")
    public Product getProductById(@PathVariable("id") long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable("category") String category) {
        if (category == null) {
            return Collections.emptyList();
        }
        return productService.getProductsByCategory(category);
    }

    @GetMapping("search/{text}")
    public List<ProductShort> getSearchedProducts(@PathVariable("text") String searchedText) {
        if (searchedText == null) {
            return Collections.emptyList();
        }
        return productService.getProductsContainingInName(searchedText);
    }
}
