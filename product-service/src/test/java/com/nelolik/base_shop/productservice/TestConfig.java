package com.nelolik.base_shop.productservice;


import com.nelolik.base_shop.productservice.mapper.ProductMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.io.Reader;

@TestConfiguration
@TestPropertySource("classpath:application.properties")
public class TestConfig {

    @Bean
    public ProductMapper productMapper() throws IOException {
        Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
        return sessionFactory.openSession().getMapper(ProductMapper.class);
    }
}
