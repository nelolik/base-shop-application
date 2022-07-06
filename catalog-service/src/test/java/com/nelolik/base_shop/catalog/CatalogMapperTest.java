package com.nelolik.base_shop.catalog;


import com.nelolik.base_shop.catalog.mapper.CatalogMapper;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@Sql(scripts = {"classpath:/db/dropTable.sql", "classpath:/db/createTable.sql", "classpath:/db/fillProductTable.sql"})
public class CatalogMapperTest {

    @Autowired
    private CatalogMapper catalogMapper;

    @Test
    void getCategoryNamesTest() {
        List<String> categories = catalogMapper.getCategoryNames();
        assertThat(categories).isNotNull().hasSize(3).containsExactlyInAnyOrder("health", "office", "food");
    }
}
