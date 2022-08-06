package com.nelolik.base_shop.statistic_service.mapper;

import com.nelolik.base_shop.statistic_service.model.UserVisitStatistic;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserVisitStatisticMapper {

    @Select("SELECT product_id FROM user_view_statistic WHERE user_id = ${userId} " +
            "ORDER BY visit_count DESC LIMIT ${count}")
    List<Long> getProductsMostOftenVisitedByUser(@Param("userId") Long userId, @Param("count") int count);

    @Select("SELECT user_id as userId, product_id as productId, visit_count as visitCount " +
            "FROM user_view_statistic " +
            "WHERE user_id = ${userId} AND product_id = ${productId}")
    UserVisitStatistic getUserProductStatistic(@Param("userId") Long userId, @Param("productId") Long productId);

    @Insert("INSERT INTO user_view_statistic (user_id, product_id, visit_count)" +
            "VALUES (${st.userId}, ${st.productId}, ${st.visitCount})")
    Long saveNewProductVisit(@Param("st") UserVisitStatistic userVisitStatistic);

    @Update("UPDATE user_view_statistic SET visit_count = ${st.visitCount} " +
            "WHERE user_id = ${st.userId} AND product_id = ${st.productId}")
    Long updateUserProductVisitCount(@Param("st") UserVisitStatistic userVisitStatistic);
}
