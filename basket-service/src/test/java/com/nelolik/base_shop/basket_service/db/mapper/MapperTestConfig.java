package com.nelolik.base_shop.basket_service.db.mapper;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.Reader;

@TestConfiguration
public class MapperTestConfig {

    @Bean
    public BasketDbMapper basketDbMapper() throws IOException {
        Reader reader = Resources.getResourceAsReader("mybatis-test-config.xml");
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
        return sessionFactory.openSession().getMapper(BasketDbMapper.class);
    }

    @Bean
    public UserBasketDbMapper userBasketDbMapper() throws IOException {
        Reader reader = Resources.getResourceAsReader("mybatis-test-config.xml");
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
        return sessionFactory.openSession().getMapper(UserBasketDbMapper.class);
    }
}
