package com.nelolik.base_shop.productservice.service;

import com.nelolik.base_shop.productservice.mapper.ProductMapper;
import com.nelolik.base_shop.productservice.model.ProductBarElement;
import com.nelolik.base_shop.productservice.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    @Override
    public List<Product> getProducts() {
        return productMapper.getProducts();
    }

    @Override
    public List<ProductBarElement> getProductsForBar() {
        return productMapper.getProductsForBar();
    }

    @Override
    public Product getProductById(long id) {
        return productMapper.getProductById(id);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productMapper.getProductsByCategory(category);
    }
}
