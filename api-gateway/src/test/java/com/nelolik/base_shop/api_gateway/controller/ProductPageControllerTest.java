package com.nelolik.base_shop.api_gateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelolik.base_shop.api_gateway.model.Product;
import com.nelolik.base_shop.api_gateway.model.ProductShort;
import com.nelolik.base_shop.api_gateway.service.ProductService;
import org.junit.jupiter.api.BeforeAll;
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

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;


    private static long ID = 1;
    private static Product p1 = new Product(ID++, "first", "first product", 100., 20,  "category1");
    private static Product p2 = new Product(ID++, "second", "second product", 50., 20, "category1");
    private static Product p3 = new Product(ID++, "third", "Best of all", 20., 55, "category2");
    private static List<Product> productList = List.of(p1, p2, p3);
    private static List<ProductShort> featuredProducts = new ArrayList<>();

    @BeforeAll
    static void init() {
        for (long i = 1; i < 5; i++) {
            featuredProducts.add(new ProductShort(i, "product" + i, 0.));
        }
    }


    @Test
    void getPopularItemsShouldReturnListOfProductShorts() throws Exception {
        when(productService.getPopularProductsForBar()).thenReturn(featuredProducts);

        mockMvc.perform(get("/advices"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(new ObjectMapper().writeValueAsString(featuredProducts))));
    }

    @Test
    void getCategoryItemsShouldReturnAllProductInThisCategory() throws Exception {
        String catName = "category1";
        List<ProductShort> category1 = productList.stream().filter(p -> p.getCategory().contains(catName))
                .map(p -> new ProductShort(p.getId(), p.getName(), p.getPrice()))
                .collect(Collectors.toList());
        when(productService.getCategoryProducts(catName)).thenReturn(category1);

        mockMvc.perform(get("/catalog/category/" + catName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(new ObjectMapper().writeValueAsString(category1))));
    }

    @Test
    void getItemByIdShouldReturnProduct() throws Exception {
        long id = 1;
        when(productService.getProductById(id)).thenReturn(p1);

        mockMvc.perform(get("/catalog/id/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(new ObjectMapper().writeValueAsString(p1))));
    }
}
