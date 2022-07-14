package com.nelolik.base_shop.catalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelolik.base_shop.catalog.controller.CatalogController;
import com.nelolik.base_shop.catalog.model.CatalogEntries;
import com.nelolik.base_shop.catalog.service.CatalogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(CatalogController.class)
public class CatalogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CatalogService catalogService;

    @MockBean
    private DataSource dataSource;

    @Test
    void getAllEntriesTest() throws Exception {
        List<String> categories = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            categories.add("entry" + i);
        }
        CatalogEntries entries = new CatalogEntries(categories);
        when(catalogService.getAllEntries()).thenReturn(entries);

        mockMvc.perform(get("/catalog/all_entries"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(new ObjectMapper().writeValueAsString(entries))));
    }
}
