package com.nelolik.base_shop.api_gateway.service;

import com.nelolik.base_shop.api_gateway.model.Product;
import com.nelolik.base_shop.api_gateway.model.ProductShort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;


@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private static final String PRODUCT_CATEGORY_URI = "http://localhost:8080/products/category/";
    private static final String PRODUCT_ID_URI = "http://localhost:8080/products/id/";

    @Override
    public List<ProductShort> getPopularProductsForBar() {
        //TODO service, which defines products, that should be on main page
        return null;
    }

    @Override
    public List<Product> getSearchedProducts(String text) {
        //TODO search response in product-service
        return null;
    }

    @Override
    public List<ProductShort> getCategoryProducts(String categoryName) {
        WebClient client = WebClient.create(PRODUCT_CATEGORY_URI + categoryName);
        Flux<ProductShort> elementFlux = client.get().retrieve()
                .bodyToFlux(ProductShort.class)
                .doOnError(e -> log.error("Error retrieve " + PRODUCT_CATEGORY_URI + categoryName
                        + " Original message: " + e.getMessage()));
        return elementFlux.collectList().block();
    }

    @Override
    public Product getProductById(long id) {
        WebClient client = WebClient.create(PRODUCT_ID_URI + id);
        return client.get().retrieve()
                .bodyToMono(Product.class)
                .doOnError(e -> log.error("Error retrieve " + PRODUCT_ID_URI + id + " Original message: " + e.getMessage()))
                .block();
    }
}
