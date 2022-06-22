package com.nelolik.productservice.mapper;

import com.nelolik.productservice.model.Product;
import com.nelolik.productservice.model.ProductBarElement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductMapper {

    @Select("SELECT * FROM product p")
    public List<Product> getProducts();

    @Select("SELECT p.id, p.name, p.price FROM product p")
    public List<ProductBarElement> getProductsForBar();


}
