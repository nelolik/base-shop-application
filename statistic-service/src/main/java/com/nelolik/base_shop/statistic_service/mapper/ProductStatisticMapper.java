package com.nelolik.base_shop.statistic_service.mapper;

import com.nelolik.base_shop.statistic_service.model.ProductStatistic;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductStatisticMapper {

    @Select("SELECT ps.id as id, ps.number_of_views as numberOfViews, ps.number_of_purchases as numberOfPurchases " +
            "FROM product_statistic ps WHERE id = #{id}")
    ProductStatistic getProductStatisticByProductId(@Param("id") Long id);

    @Insert("INSERT INTO product_statistic VALUES (#{p.id}, #{p.numberOfViews}, #{p.numberOfPurchases})")
    long saveProductStatistic(@Param("p") ProductStatistic productStatistic);

    @Update("UPDATE product_statistic SET number_of_views = #{p.numberOfViews}, " +
            "number_of_purchases = #{p.numberOfPurchases} WHERE id = #{p.id}")
    long updateProductStatistic(@Param("p") ProductStatistic productStatistic);

    @Select("SELECT * FROM product_statistic ORDER BY number_of_views DESC LIMIT #{count}")
    List<Long> getMostViewedProductIds(@Param("count") long count);


}
