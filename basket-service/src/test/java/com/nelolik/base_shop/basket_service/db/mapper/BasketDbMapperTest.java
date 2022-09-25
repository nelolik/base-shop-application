package com.nelolik.base_shop.basket_service.db.mapper;


import com.nelolik.base_shop.basket_service.dto.BasketDBO;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@Sql(scripts = {"classpath:/db/createBasketTable.sql", "classpath:/db/fillBasketTable.sql"})
public class BasketDbMapperTest {

    @Autowired
    private BasketDbMapper mapper;

    private BasketDBO record1 = new BasketDBO(1l, 1l, "name 1", BigDecimal.valueOf(1020, 2), 2);
    private BasketDBO record2 = new BasketDBO(1l, 2l, "name 2", BigDecimal.valueOf(5.31), 1);
    private BasketDBO record3 = new BasketDBO(1l, 3l, "name 3", BigDecimal.valueOf(21.74), 1);
    private BasketDBO record4 = new BasketDBO(2l, 2l, "name 2", BigDecimal.valueOf(5.31), 2);
    private BasketDBO record5 = new BasketDBO(2l, 4l, "name 4", BigDecimal.valueOf(1.17), 5);
    private BasketDBO record6 = new BasketDBO(3l, 5l, "name 5", BigDecimal.valueOf(4340, 2), 1);
    private List<BasketDBO> dbContent = List.of(record1, record2, record3, record4, record5, record6);
    private List<BasketDBO> basket1 = List.of(record1, record2, record3);
    private List<BasketDBO> basket2 = List.of(record4, record5);
    private List<BasketDBO> basket3 = List.of(record6);

    @Test
    void getBasketByIdTest() {
        long basketId = 1;

        List<BasketDBO> result = mapper.getBasketItemsById(basketId);

        assertThat(result).isNotNull().containsExactlyInAnyOrderElementsOf(basket1);
    }

    @Test
    void getBasketByIdWithWrongIdReturnsEmptyList() {
        long basketId = 100;

        List<BasketDBO> result = mapper.getBasketItemsById(basketId);

        assertThat(result).isEmpty();
    }

    @Test
    void saveBasketTest() {
        long basketId = 5;
        BasketDBO newRec1 = new BasketDBO(basketId, 7l, "new name 1", BigDecimal.valueOf(17.27), 2);
        BasketDBO newRec2 = new BasketDBO(basketId, 8l, "new name 2", BigDecimal.valueOf(15.68), 1);
        BasketDBO newRec3 = new BasketDBO(basketId, 9l, "new name 3", BigDecimal.valueOf(21.74), 4);
        List<BasketDBO> newBasket = List.of(newRec1, newRec2, newRec3);

        mapper.saveBasket(newBasket);

        List<BasketDBO> saved = mapper.getBasketItemsById(basketId);
        assertThat(saved).isNotNull().containsExactlyInAnyOrderElementsOf(newBasket);
    }

    @Test
    void updateBasketTest() {
        long basketId = 2;
        List<BasketDBO> originItems = mapper.getBasketItemsById(basketId);
        assertThat(originItems).isNotEmpty();
        String updatedName = "updated name";
        BasketDBO originRecord = originItems.get(0);
        List<BasketDBO> updatedItems = new ArrayList<>(originItems);
        updatedItems.remove(0);
        updatedItems.add(new BasketDBO(originRecord.getBasketId(), originRecord.getProductId(), updatedName,
                originRecord.getProductPrice(), originRecord.getQuantity()));

        updatedItems.forEach(basketDBO -> mapper.updateBasket(basketDBO));

        List<BasketDBO> updated = mapper.getBasketItemsById(2l);
        assertThat(updated).isNotNull().containsExactlyInAnyOrderElementsOf(updatedItems);
    }

    @Test
    void deleteItemByIdTest() {
         long productId = 2;
         long basketId = 1;

        List<BasketDBO> beforeDelete = mapper.getBasketItemsById(basketId);
        assertThat(beforeDelete).isNotNull().extracting(BasketDBO::getProductId).contains(productId);

        mapper.deleteItemById(productId, basketId);

        List<BasketDBO> afterDelete = mapper.getBasketItemsById(basketId);
        assertThat(afterDelete).isNotNull().extracting(BasketDBO::getProductId).doesNotContain(productId);

        List<BasketDBO> otherBasket = mapper.getBasketItemsById(2L);
        assertThat(otherBasket).isNotNull().extracting(BasketDBO::getProductId).contains(productId);
    }

    @Test
    void deleteBasketTest() {
        Long basketId = basket3.get(0).getBasketId();

        List<BasketDBO> beforeDelete = mapper.getBasketItemsById(basketId);
        assertThat(beforeDelete).isNotNull().containsExactlyInAnyOrderElementsOf(basket3);

        mapper.deleteBasket(basketId);

        List<BasketDBO> afterDelete = mapper.getBasketItemsById(basketId);

        assertThat(afterDelete).isEmpty();
    }
}
