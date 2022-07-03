package com.nelolik.base_shop.productservice.service;

import com.nelolik.base_shop.productservice.model.Product;
import com.nelolik.base_shop.productservice.model.ProductBarElement;

import java.util.List;

public interface ProductService {

    List<Product> getProducts();

    List<ProductBarElement> getProductsForBar();

    Product getProductById(long id);

    List<Product> getProductsByCategory(String category);
}
