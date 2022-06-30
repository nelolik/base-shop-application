package com.nelolik.productservice.mapper;

import com.nelolik.productservice.model.Product;
import com.nelolik.productservice.model.ProductBarElement;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper {

    @Select("SELECT * FROM product p")
    List<Product> getProducts();

    @Select("SELECT p.id, p.name, p.price FROM product p")
    List<ProductBarElement> getProductsForBar();

    @Select("SELECT * FROM product WHERE id = #{id}")
    Product getProductById(@Param("id") Long id);

    @Insert("INSERT INTO product VALUES (#{p.id}, #{p.name}, #{p.description}, #{p.price}, #{p.quantity}, #{p.category})")
    void saveProduct(@Param("p") Product product);

    @Update("UPDATE product SET name = #{p.name}, description = #{p.description}, price = #{p.price}, " +
            "quantity = #{p.quantity}, category = #{p.category} WHERE id = #{p.id}")
    void updateProduct(@Param("p") Product product);
}
