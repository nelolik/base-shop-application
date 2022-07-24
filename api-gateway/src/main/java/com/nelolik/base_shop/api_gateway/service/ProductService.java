package com.nelolik.base_shop.api_gateway.service;

import com.nelolik.base_shop.api_gateway.model.Product;
import com.nelolik.base_shop.api_gateway.model.ProductShort;

import java.util.List;

public interface ProductService {

    List<ProductShort> getPopularProductsForBar();

    List<ProductShort> getSearchedProducts(String text);

    List<ProductShort> getCategoryProducts(String categoryName);

    Product getProductById(long id);

}
