package com.nelolik.base_shop.api_gateway.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelolik.base_shop.api_gateway.model.ProductShort;
import com.nelolik.base_shop.api_gateway.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private long ID = 1;
    private ProductShort p1 = new ProductShort(ID++, "first", 0.);
    private ProductShort p2 = new ProductShort(ID++, "second", 0.);
    private ProductShort p3 = new ProductShort(ID++, "third", 0.);
    private List<ProductShort> productList = List.of(p1, p2, p3);


    @Test
    void getSearchedItemsShouldReturnListOfMatchingProducts() throws Exception {
        String pattern = "ir";
        List<ProductShort> searchResult = productList.stream().filter(p -> p.getName().contains(pattern))
                .collect(Collectors.toList());
        when(productService.getSearchedProducts(pattern)).thenReturn(searchResult);

        mockMvc.perform(get("/search/" + pattern))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(new ObjectMapper().writeValueAsString(searchResult))));
    }

    @Test
    void getSearchedItemsShouldReturnEmptyListWithWrongPattern() throws Exception {
        when(productService.getSearchedProducts("wrong")).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/search/wrong"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string("[]"));
    }

}
