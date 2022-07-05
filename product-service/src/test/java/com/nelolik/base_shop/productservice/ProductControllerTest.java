package com.nelolik.base_shop.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelolik.base_shop.productservice.mapper.ProductMapper;
import com.nelolik.base_shop.productservice.model.Product;
import com.nelolik.base_shop.productservice.model.ProductShort;
import com.nelolik.base_shop.productservice.service.ProductService;
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
    private ProductService productService;

    private static long ID = 1;
    private static Product p1 = new Product(ID++, "toothpaste", "gel too clean tooth",
            200., 22, "health");
    private static Product p2 = new Product(ID++, "pen", "a thing to write on paper",
            80., 77, "office");
    private static Product p3 = new Product(ID++, "beacon", "tasty thing",
            350., 7, "food");
    private static List<Product> productList = List.of(p1, p2, p3);
    private static List<ProductShort> productShortList = new ArrayList<>();

    @BeforeAll
    static void init() {
        for (Product p :
                productList) {
            productShortList.add(new ProductShort(p.getId(), p.getName(), p.getPrice()));
        }
    }

    @Test
    void getProductsShouldReturnListOfProducts() throws Exception {
        when(productService.getProducts()).thenReturn(productList);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/json")))
                .andExpect(content().string(containsString(new ObjectMapper().writeValueAsString(productList))));

    }

    @Test
    void getProductForBarShouldReturnListOfProductShort() throws Exception {
        when(productService.getProductsForBar()).thenReturn(productShortList);

        mockMvc.perform(get("/products/for_bar"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(new ObjectMapper().writeValueAsString(productShortList))));
    }

    @Test
    void getProductByIdShouldReturnSingleProduct() throws Exception {
        long id = 1;
        when(productService.getProductById(id)).thenReturn(p1);

        mockMvc.perform(get("/products/id/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(new ObjectMapper().writeValueAsString(p1))));
    }

    @Test
    void getSearchedProductsShouldReturnMatchingProducts() throws Exception {
        String searchText = "e";
        when(productService.getProductsContainingInName(searchText)).thenReturn(productShortList);

        mockMvc.perform(get("/products/search/" + searchText))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(new ObjectMapper().writeValueAsString(productShortList))));

    }

}
