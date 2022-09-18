package com.nelolik.base_shop.basket_service.db.mapper;

import org.apache.ibatis.annotations.*;

import java.time.OffsetDateTime;
import java.util.List;

@Mapper
public interface UserBasketDbMapper {

    @Select("SELECT basket_id FROM user_basket WHERE user_id=#{userId}")
    Long getBasketIdForUser(@Param("userId") long userId);

    @Select("SELECT creation_time FROM user_basket WHERE user_id=#{userId}")
    OffsetDateTime getCreationTimeByUserId(@Param("userId") long userid);

    @Insert("INSERT INTO user_basket (user_id, creation_time) VALUES(#{userId}, #{time})")
    Long addNewBasketForUser(@Param("userId") long userId, @Param("time") OffsetDateTime creationTime);

    @Delete("DELETE FROM user_basket WHERE basket_id=#{basketId}")
    void removeBasketByBasketId(@Param("basketId") long basketId);

    @Delete("DELETE FROM user_basket WHERE user_id=#{userId}")
    void removeBasketsForUser(@Param("userId") long userId);

    @Select("SELECT basket_id FROM user_basket ORDER BY basket_id DESC LIMIT 10")
    List<Long> getLatestBasketIds();
}
