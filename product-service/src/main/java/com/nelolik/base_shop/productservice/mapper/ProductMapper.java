package com.nelolik.base_shop.productservice.mapper;

import com.nelolik.base_shop.productservice.model.Product;
import com.nelolik.base_shop.productservice.model.ProductShort;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper {

    @Select("SELECT * FROM product p")
    List<Product> getProducts();

    @Select("SELECT p.id, p.name, p.price FROM product p LIMIT 50")
    List<ProductShort> getProductsForBar();

    @Select("SELECT * FROM product WHERE id = #{id}")
    Product getProductById(@Param("id") Long id);

    @Select("SELECT * FROM product WHERE category = #{category}")
    List<Product> getProductsByCategory(@Param("category") String category);

    @Insert("INSERT INTO product VALUES (#{p.id}, #{p.name}, #{p.description}, #{p.price}, #{p.quantity}, #{p.category})")
    void saveProduct(@Param("p") Product product);

    @Update("UPDATE product SET name = #{p.name}, description = #{p.description}, price = #{p.price}, " +
            "quantity = #{p.quantity}, category = #{p.category} WHERE id = #{p.id}")
    void updateProduct(@Param("p") Product product);

    @Select("SELECT p.id, p.name, p.price FROM product p WHERE p.name LIKE '%'||#{s}||'%'")
    List<ProductShort> findProductsContainingInName(@Param("s") String searchText);

    @Select("<script> SELECT p.id, p.name, p.price FROM product p WHERE id IN " +
            "    <foreach item='item' index='index' collection='ids' " +
                    " open='(' separator=',' close=')'> " +
                    " #{item} " +
            "    </foreach> </script>")
    List<ProductShort> findProductShortsByIds(@Param("ids") List<Long> ids);

    @Select("SELECT p.id, p.name, p.price FROM product p WHERE id=#{id}")
    ProductShort getProductShortById(@Param("id") Long id);
}
