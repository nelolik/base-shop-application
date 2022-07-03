package com.nelolik.productservice.controller;

import com.nelolik.productservice.mapper.ProductMapper;
import com.nelolik.productservice.model.Product;
import com.nelolik.productservice.model.ProductBarElement;
import com.nelolik.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
            return new ArrayList<>();
        }
        return products;
    }

    @GetMapping("/for_bar")
    public List<ProductBarElement> getProductListForBar() {
        List<ProductBarElement> products = null;
        products = productService.getProductsForBar();
        if (products == null) {
            products = new ArrayList<>();
        }
        return products;
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable("id") long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable("category") String category) {
        if (category == null) {
            return new ArrayList<>();
        }
        return productService.getProductsByCategory(category);
    }


}
