package com.nelolik.base_shop.catalog.controller;

import com.nelolik.base_shop.catalog.model.CatalogEntries;
import com.nelolik.base_shop.catalog.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping("/all_entries")
    public CatalogEntries getAllEntries() {
        return catalogService.getAllEntries();
    }
}
