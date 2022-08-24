package com.nelolik.base_shop.api_gateway.service;

import com.nelolik.base_shop.api_gateway.config.ApiUris;
import com.nelolik.base_shop.api_gateway.model.Product;
import com.nelolik.base_shop.api_gateway.model.ProductShort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;


@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ApiUris uris;

    @Override
    public List<ProductShort> getPopularProductsForBar() {
        //TODO service, which defines products, that should be on main page
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<ProductShort> getSearchedProducts(String text) {
        WebClient client = WebClient.create(uris.getProductSearch() + text);
        return client.get().retrieve()
                .bodyToFlux(ProductShort.class)
                .doOnError(e -> log.error("Error retrieve " + uris.getProductSearch() + text
                        + " Original message: " + e.getMessage()))
                .collectList().block();
    }

    @Override
    public List<ProductShort> getCategoryProducts(String categoryName) {
        WebClient client = WebClient.create(uris.getProductCategory() + categoryName);
        Flux<ProductShort> elementFlux = client.get().retrieve()
                .bodyToFlux(ProductShort.class)
                .doOnError(e -> log.error("Error retrieve " + uris.getProductCategory() + categoryName
                        + " Original message: " + e.getMessage()));
        return elementFlux.collectList().block();
    }

    @Override
    public Product getProductById(long id) {
        WebClient client = WebClient.create(uris.getProductId() + id);
        return client.get().retrieve()
                .bodyToMono(Product.class)
                .doOnError(e -> log.error("Error retrieve " + uris.getProductId() + id + " Original message: " + e.getMessage()))
                .block();
    }

}
