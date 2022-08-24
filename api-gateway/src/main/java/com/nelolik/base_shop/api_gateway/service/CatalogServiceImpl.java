package com.nelolik.base_shop.api_gateway.service;

import com.nelolik.base_shop.api_gateway.config.ApiUris;
import com.nelolik.base_shop.api_gateway.model.CatalogEntries;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@Slf4j
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private ApiUris uris;
    @Override
    public CatalogEntries getProductCatalogEntries() {
        WebClient webClient = WebClient.create(uris.getCatalog());
        return webClient.get().retrieve()
                .bodyToMono(CatalogEntries.class)
                .doOnError(e -> log.error("Error retrieve " + uris.getCatalog() + " Original message: " + e.getMessage()))
                .block();
    }
}
