package com.nelolik.productservice.controller;

import com.nelolik.productservice.mapper.ProductMapper;
import com.nelolik.productservice.model.Product;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @GetMapping()
    public Product getProducts() {
                SqlSessionFactory sessionFactory;
        ProductMapper productMapper;
        Reader reader;
        try {
            reader = Resources.getResourceAsReader("mybatis-config.xml");
            sessionFactory = new SqlSessionFactoryBuilder().build(reader);
            productMapper = sessionFactory.openSession().getMapper(ProductMapper.class);
            List<Product> products = productMapper.getProducts();
            if (products != null && products.size() > 0) {
                return products.get(0);
            } else {
                return new Product();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Product();
    }

}
