package com.nelolik.base_shop.productservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelolik.base_shop.productservice.model.ProductShort;
import com.nelolik.base_shop.productservice.service.ProductService;
import com.nelolik.base_shop.productservice.model.Product;
import com.nelolik.base_shop.productservice.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final StatisticService statisticService;

    private final ObjectMapper objectMapper;

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

    @GetMapping("/for_bar/with_ids")
    public List<ProductShort> getProductShortsWithRequestedIds(@RequestParam("ids") String ids) {
        String[] splited = ids.split(",");
        List<Long> requestedIds = Arrays.stream(splited).filter(NumberUtils::isCreatable)
                .map(Long::valueOf).collect(Collectors.toList());
        return productService.getProductShortsByListOfId(requestedIds);
    }

    @GetMapping("/id/{id}")
    public Product getProductById(@PathVariable("id") long id) {
        Product product = productService.getProductById(id);
        statisticService.saveProductVisitWithoutUserInfo(product);
        return product;
    }

    @GetMapping("/short/id/{id}")
    public ProductShort getProductShortById(@PathVariable("id") long id) {
        return productService.getProductShortById(id);
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
