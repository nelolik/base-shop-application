package com.nelolik.base_shop.catalog;

import com.nelolik.base_shop.catalog.mapper.CatalogMapper;
import com.nelolik.base_shop.catalog.model.CatalogEntries;
import com.nelolik.base_shop.catalog.service.CatalogService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
public class CatalogServiceTest {

    @Autowired
    private CatalogService catalogService;

    @MockBean
    private CatalogMapper mapper;

    @Test
    void getAllEntriesTest() {
        List<String> categories = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            categories.add("entry" + i);
        }
        CatalogEntries entries = new CatalogEntries(categories);

        when(mapper.getCategoryNames()).thenReturn(categories);

        CatalogEntries result = catalogService.getAllEntries();
        Assertions.assertEquals(entries, result);
    }
}
