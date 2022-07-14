package com.nelolik.base_shop.api_gateway.service;

import com.nelolik.base_shop.api_gateway.model.CatalogEntries;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@Slf4j
public class CatalogServiceImpl implements CatalogService {

    private static final String CATALOG_URL = "http://localhost:8082/catalog/all_entries";
    @Override
    public CatalogEntries getProductCatalogEntries() {
        WebClient webClient = WebClient.create(CATALOG_URL);
        return webClient.get().retrieve()
                .bodyToMono(CatalogEntries.class)
                .doOnError(e -> log.error("Error retrieve " + CATALOG_URL + " Original message: " + e.getMessage()))
                .block();
    }
}
