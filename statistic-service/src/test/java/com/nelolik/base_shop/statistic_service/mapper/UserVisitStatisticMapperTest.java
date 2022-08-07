package com.nelolik.base_shop.statistic_service.mapper;

import com.nelolik.base_shop.statistic_service.model.UserVisitStatistic;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@Sql(scripts = {"classpath:/create_user_statistic_table.sql", "classpath:/fill_user_statistic_table.sql"})
public class UserVisitStatisticMapperTest {

    @Autowired
    private UserVisitStatisticMapper mapper;

    @Test
    void getProductsMostOftenVisitedByUserShouldReturnListOfIds() {
        long userId = 1;
        List<Long> expected = List.of(6L, 1L, 3L, 2L, 5L, 4L);
        List<Long> productsMostOftenVisitedByUser = mapper.getProductsMostOftenVisitedByUser(userId, expected.size());

        assertThat(productsMostOftenVisitedByUser).isNotNull().containsExactlyElementsOf(expected);
    }

    @Test
    void getUserProductStatisticTest() {
        long userId = 1;
        long productId = 1;
        long visitCount = 20;
        UserVisitStatistic expected = new UserVisitStatistic(userId, productId, visitCount);

        UserVisitStatistic result = mapper.getUserProductStatistic(userId, productId);

        assertThat(result).isNotNull().isEqualTo(expected);
    }
}
