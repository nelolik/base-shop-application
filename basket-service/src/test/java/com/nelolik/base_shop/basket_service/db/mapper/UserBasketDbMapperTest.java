package com.nelolik.base_shop.basket_service.db.mapper;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


@MybatisTest
@Sql(scripts = {"classpath:/db/createUserBasketTable.sql", "classpath:/db/fillUserBasketTable.sql"})
public class UserBasketDbMapperTest {

    @Autowired
    private UserBasketDbMapper mapper;

    @Test
    void addNewBasketForUserTest() {
        long userId = 5;
        OffsetDateTime timeNow = OffsetDateTime.now();
        List<Long> existingBasketIds = mapper.getLatestBasketIds();
        long maxOldId = existingBasketIds.stream().mapToLong(l -> l).max().getAsLong();

        mapper.addNewBasketForUser(userId, timeNow);

        Long newBasketId = mapper.getBasketIdForUser(userId);
        List<Long> basketIdsAfterAdding = mapper.getLatestBasketIds();

        Assertions.assertEquals(newBasketId, maxOldId + 1);
        assertThat(basketIdsAfterAdding).contains(newBasketId);
        assertThat(existingBasketIds).doesNotContain(newBasketId);
    }

    @Test
    void getBasketIdForExistingUser() {
        long userId = 1;
        long expectedBasketId = 1;

        Long actualBasketId = mapper.getBasketIdForUser(userId);

        Assertions.assertEquals(expectedBasketId, actualBasketId);
    }

    @Test
    void getBasketIdForNotExistingUser() {
        long userId = 1000;

        Long actualBasketId = mapper.getBasketIdForUser(userId);

        Assertions.assertNull(actualBasketId);
    }

    @Test
    void getCreationTimeByUserIdTest() {
        long userId = 5;
        OffsetDateTime creationTime = OffsetDateTime.now().minusHours(2).truncatedTo(ChronoUnit.SECONDS);
        mapper.addNewBasketForUser(userId, creationTime);

        OffsetDateTime actualTime = mapper.getCreationTimeByUserId(userId);

        Assertions.assertEquals(creationTime, actualTime);
    }

    @Test
    void removeBasketByBasketIdTest() {
        Long existingId = mapper.getLatestBasketIds().get(0);

        mapper.removeBasketByBasketId(existingId);

        List<Long> afterDeletion = mapper.getLatestBasketIds();
        assertThat(afterDeletion).doesNotContain(existingId);
    }

    @Test
    void removeBasketsForUserTest() {
        long userId = 2;
        Long existingId = mapper.getBasketIdForUser(userId);

        mapper.removeBasketsForUser(userId);

        Long afterDeletion = mapper.getBasketIdForUser(userId);

        Assertions.assertNotNull(existingId);
        Assertions.assertNull(afterDeletion);
    }

    @Test
    void getLatestBasketIdsTest() {
        List<Long> expectedIds = List.of(1l, 2l, 3l);

        List<Long> actualIds = mapper.getLatestBasketIds();

        assertThat(actualIds).isNotNull().containsExactlyInAnyOrderElementsOf(expectedIds);
    }
}
