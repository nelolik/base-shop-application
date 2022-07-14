package com.nelolik.base_shop.catalog.config;


import com.nelolik.base_shop.catalog.mapper.CatalogMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.Reader;

@Configuration
public class CatalogConfig {

    @Bean
    CatalogMapper productMapper() throws IOException {
        Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
        return sessionFactory.openSession().getMapper(CatalogMapper.class);
    }
}
