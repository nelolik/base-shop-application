package com.nelolik.base_shop.statistic_service.mapper;

import com.nelolik.base_shop.statistic_service.model.ProductStatistic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;


@MybatisTest
//@ContextConfiguration(classes = {TestConfig.class})
@Sql(scripts = {"classpath:/drop_statistic_table.sql", "classpath:/create_table.sql",
        "classpath:/fill_statistic_table.sql"})
public class ProductStatisticMapperTest {

    @Autowired
    private ProductStatisticMapper productStatisticMapperTest;

    @Test
    void testGetProductStatisticById() {
        ProductStatistic expected = new ProductStatistic(2L, 100L, 30L);

        ProductStatistic result = productStatisticMapperTest.getProductStatisticByProductId(2L);

        Assertions.assertEquals(expected, result);
    }


    @Test
    void testSaveProductStatistic() {
        ProductStatistic newProduct = new ProductStatistic(10L, 0L, 0L);

        productStatisticMapperTest.saveProductStatistic(newProduct);
        ProductStatistic received = productStatisticMapperTest.getProductStatisticByProductId(newProduct.getId());

        Assertions.assertEquals(newProduct, received, "Saved and read records do not match.");
    }

    @Test
    void testUpdateProductStatistic() {
        ProductStatistic oldRecord = productStatisticMapperTest.getProductStatisticByProductId(3L);
        ProductStatistic newRecord = new ProductStatistic(oldRecord.getId(), oldRecord.getNumberOfViews() + 5,
                oldRecord.getNumberOfPurchases() + 7);

        productStatisticMapperTest.updateProductStatistic(newRecord);
        ProductStatistic received = productStatisticMapperTest.getProductStatisticByProductId(oldRecord.getId());

        Assertions.assertEquals(newRecord, received, "Updated and read records do not match.");
    }


}
