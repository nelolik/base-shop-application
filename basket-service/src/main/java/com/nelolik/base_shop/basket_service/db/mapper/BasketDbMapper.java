package com.nelolik.base_shop.basket_service.db.mapper;

import com.nelolik.base_shop.basket_service.model.basket.BasketDBO;
import com.nelolik.base_shop.basket_service.model.basket.BasketDb;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BasketDbMapper extends BasketDb {

    @Select("SELECT * FROM basket WHERE basket_id = #{basketId}")
    List<BasketDBO> getBasketItemsById(@Param("basketId") long basketId);

    @Insert("<script>INSERT INTO basket VALUES " +
            " <foreach item='item' index='index' collection='dbos' separator=','> " +
                "(#{item.basketId}, #{item.productId}, #{item.productName}, #{item.productPrice}, #{item.quantity}) " +
            " </foreach> </script>")
    void saveBasket(@Param("dbos") List<BasketDBO> basketDBOS);

    @Update("UPDATE basket SET " +
            "product_name=#{dbo.productName}, product_price=#{dbo.productPrice}, quantity=#{dbo.quantity} " +
            "WHERE basket_id=#{dbo.basketId} AND product_id=#{dbo.productId} ")
    void updateBasket(@Param("dbo") BasketDBO basketDBO);

    @Delete("DELETE FROM basket WHERE basket_id = #{basketId}")
    void deleteBasket(@Param("basketId") long basketId);
}
