package com.nelolik.base_shop.productservice.service;

import com.nelolik.base_shop.productservice.model.Product;
import com.nelolik.base_shop.productservice.model.ProductShort;

import java.util.List;

public interface ProductService {

    List<Product> getProducts();

    List<ProductShort> getProductsForBar();

    Product getProductById(long id);

    List<Product> getProductsByCategory(String category);

    List<ProductShort> getProductsContainingInName(String text);
}
