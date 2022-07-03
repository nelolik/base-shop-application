package com.nelolik.productservice.service;

import com.nelolik.productservice.model.Product;
import com.nelolik.productservice.model.ProductBarElement;

import java.util.List;

public interface ProductService {

    List<Product> getProducts();

    List<ProductBarElement> getProductsForBar();

    Product getProductById(long id);

    List<Product> getProductsByCategory(String category);
}
