package com.nelolik.base_shop.catalog.service;

import com.nelolik.base_shop.catalog.mapper.CatalogMapper;
import com.nelolik.base_shop.catalog.model.CatalogEntries;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private final CatalogMapper catalogMapper;

    @Override
    public CatalogEntries getAllEntries() {
        return new CatalogEntries(catalogMapper.getCategoryNames());
    }

}
