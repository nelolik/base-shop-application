package com.nelolik.base_shop.statistic_service.config;


import com.google.common.cache.CacheBuilder;
import com.nelolik.base_shop.statistic_service.mapper.ProductStatisticMapper;
import com.nelolik.base_shop.statistic_service.mapper.UserVisitStatisticMapper;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableRabbit
public class StatisticServiceConfig {

    @Value("${queueName}")
    private String queueName;

    @Value("${queueHost")
    private String queueHost;

    @Value("${cacheExpiresMinutes}")
    private int  cacheExpiresMinutes;

    @Autowired
    private Environment environment;

    @Bean
    Queue statisticQueue() {
        return new Queue(queueName  , false);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(queueHost);
        return factory;
    }

    @Bean
    ProductStatisticMapper productMapper() throws IOException {
        Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
        return sessionFactory.openSession().getMapper(ProductStatisticMapper.class);
    }

    @Bean
    UserVisitStatisticMapper userVisitStatisticMapper() throws IOException {
        Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
        return sessionFactory.openSession().getMapper(UserVisitStatisticMapper.class);
    }

    @Bean
    CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(CacheNames.PRODUCT_STATISTIC, CacheNames.MOST_VISITED,
                CacheNames.MOST_VIEWED_BY_USER) {
            @Override
            protected Cache createConcurrentMapCache(String name) {
                return new ConcurrentMapCache(name,
                        CacheBuilder.newBuilder().expireAfterWrite(cacheExpiresMinutes, TimeUnit.MINUTES).build().asMap(),
                        true);
            }
        };
    }
}
