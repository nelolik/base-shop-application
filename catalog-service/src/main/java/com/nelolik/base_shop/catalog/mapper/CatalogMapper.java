package com.nelolik.base_shop.catalog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CatalogMapper {

    @Select("SELECT DISTINCT p.category FROM product p")
    List<String> getCategoryNames();

}
