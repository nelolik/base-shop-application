package com.nelolik.base_shop.basket_service.db.mapper;

import com.nelolik.base_shop.basket_service.model.basket.BasketDb;
import com.nelolik.base_shop.basket_service.dto.BasketDBO;
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

    @Override
    @Delete("DELETE FROM basket WHERE product_id=#{productId} AND basket_id=#{basketId}")
    void deleteItemById(@Param("productId") long productId, @Param("basketId") long basketId);

    @Delete("DELETE FROM basket WHERE basket_id = #{basketId}")
    void deleteBasket(@Param("basketId") long basketId);
}
