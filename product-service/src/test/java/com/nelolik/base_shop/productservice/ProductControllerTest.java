package com.nelolik.base_shop.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelolik.base_shop.productservice.model.Product;
import com.nelolik.base_shop.productservice.model.ProductShort;
import com.nelolik.base_shop.productservice.service.ProductService;
import com.nelolik.base_shop.productservice.service.StatisticService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
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

    @MockBean
    private StatisticService statisticService;

    private static long ID = 1;
    private static final Product p1 = new Product(ID++, "toothpaste", "gel too clean tooth",
            BigDecimal.valueOf(200.10), 22, "health");
    private static final Product p2 = new Product(ID++, "pen", "a thing to write on paper",
            BigDecimal.valueOf(80.35), 77, "office");
    private static final Product p3 = new Product(ID++, "beacon", "tasty thing",
            BigDecimal.valueOf(349.99), 7, "food");

    private static final Product p4 = new Product(ID++, "bread", "bakery product obtained by baking dough",
            BigDecimal.valueOf(65.40), 40, "food");
    private static final List<Product> productList = List.of(p1, p2, p3, p4);
    private static final List<ProductShort> productShortList = new ArrayList<>();

    private ObjectMapper objectMapper = new ObjectMapper();

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
                .andExpect(content().string(containsString(objectMapper.writeValueAsString(productList))));

    }

    @Test
    void getProductForBarShouldReturnListOfProductShort() throws Exception {
        when(productService.getProductsForBar()).thenReturn(productShortList);

        mockMvc.perform(get("/products/for_bar"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(objectMapper.writeValueAsString(productShortList))));
    }

    @Test
    void getProductByIdShouldReturnSingleProduct() throws Exception {
        long id = 1;
        when(productService.getProductById(id)).thenReturn(p1);

        mockMvc.perform(get("/products/id/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(objectMapper.writeValueAsString(p1))));

        verify(productService, times(1)).getProductById(id);
        verify(statisticService, times(1)).saveProductVisitWithoutUserInfo(p1);
    }

    @Test
    void getProductsByCategoryShouldReturnAllProductsFromExistingCategory() throws Exception {
        String category = "food";
        List<Product> food = productList.stream().filter(p -> category.equals(p.getCategory())).toList();

        when(productService.getProductsByCategory(category)).thenReturn(food);

        mockMvc.perform(get("/products/category/" + category))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(objectMapper.writeValueAsString(food))));

        verify(productService, times(1)).getProductsByCategory(category);
    }

    @Test
    void getProductsByCategoryShouldReturnEmptyListWhenCategoryIsAbsent() throws Exception {
        String category = "other";
        List<Product> expected = Collections.emptyList();

        when(productService.getProductsByCategory(category)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/products/category/" + category))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(objectMapper.writeValueAsString(expected))));

        verify(productService, times(1)).getProductsByCategory(anyString());
    }

    @Test
    void getSearchedProductsShouldReturnMatchingProducts() throws Exception {
        String searchText = "e";
        when(productService.getProductsContainingInName(searchText)).thenReturn(productShortList);

        mockMvc.perform(get("/products/search/" + searchText))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(objectMapper.writeValueAsString(productShortList))));

    }

}
