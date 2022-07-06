package com.nelolik.base_shop.api_gateway.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.nelolik.base_shop.api_gateway.model.CatalogEntries;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;

@SpringBootTest
public class CatalogServiceTest {

    @Autowired
    private CatalogService catalogService;

    private static int port = 8082;
    private static WireMockServer server;


    @BeforeAll
    static void init() {
        server = new WireMockServer(port);
        server.start();
    }

    @AfterAll
    static void after() {
        server.stop();
    }

    @Test
    void getProductCatalogEntriesTest() throws JsonProcessingException {
        CatalogEntries entries = new CatalogEntries(List.of("entry1", "entry2", "entry3"));

        server.stubFor(get("/catalog/all_entries")
                .willReturn(aResponse().withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody(new ObjectMapper().writeValueAsString(entries))));

        CatalogEntries result = catalogService.getProductCatalogEntries();
        Assertions.assertEquals(entries, result);
    }

}
