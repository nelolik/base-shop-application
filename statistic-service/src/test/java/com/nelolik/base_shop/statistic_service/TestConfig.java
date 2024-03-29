package com.nelolik.base_shop.statistic_service;

import com.google.common.cache.CacheBuilder;
import com.nelolik.base_shop.statistic_service.config.CacheNames;
import com.nelolik.base_shop.statistic_service.mapper.ProductStatisticMapper;
import com.nelolik.base_shop.statistic_service.mapper.UserVisitStatisticMapper;
import com.nelolik.base_shop.statistic_service.model.UserVisitStatistic;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.TimeUnit;

@TestConfiguration
@TestPropertySource("classpath:test.properties")
public class TestConfig {

    @Bean
    ProductStatisticMapper productStatisticMapper() throws IOException {
        Reader reader = Resources.getResourceAsReader("mybatis-test-config.xml");
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
        return sessionFactory.openSession().getMapper(ProductStatisticMapper.class);
    }

    @Bean
    UserVisitStatisticMapper userVisitStatisticMapper() throws IOException {
        Reader reader = Resources.getResourceAsReader("mybatis-test-config.xml");
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
        return sessionFactory.openSession().getMapper(UserVisitStatisticMapper.class);
    }

}
