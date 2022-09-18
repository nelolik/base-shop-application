package com.nelolik.base_shop.basket_service.db;

import com.nelolik.base_shop.basket_service.db.mapper.BasketDbMapper;
import com.nelolik.base_shop.basket_service.db.mapper.UserBasketDbMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.Reader;

@Configuration
public class DbConfig {

    @Bean
    public BasketDbMapper basketDbMapper() throws IOException {
        Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
        return sessionFactory.openSession().getMapper(BasketDbMapper.class);
    }

    @Bean
    public UserBasketDbMapper userBasketDbMapper() throws IOException {
        Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
        return sessionFactory.openSession().getMapper(UserBasketDbMapper.class);
    }
}
