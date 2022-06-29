package com.nelolik.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelolik.productservice.controller.ProductController;
import com.nelolik.productservice.mapper.ProductMapper;
import com.nelolik.productservice.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductMapper productMapper;

    private static long ID = 1;
    private static Product p1 = new Product(ID++, "toothpaste", "gel too clean tooth",
            200., 22, "health");
    private static Product p2 = new Product(ID++, "pen", "a thing to write on paper",
            80., 77, "office");
    private static Product p3 = new Product(ID++, "beacon", "tasty thing",
            350., 7, "food");
    private static List<Product> productList = List.of(p1, p2, p3);

    @Test
    void getProductsShouldReturnListOfProducts() throws Exception {
        when(productMapper.getProducts()).thenReturn(productList);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/json")))
                .andExpect(content().string(containsString(new ObjectMapper().writeValueAsString(productList))));

    }

}
