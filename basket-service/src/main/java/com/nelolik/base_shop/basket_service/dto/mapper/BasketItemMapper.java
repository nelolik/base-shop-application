package com.nelolik.base_shop.basket_service.dto.mapper;

import com.nelolik.base_shop.basket_service.dto.BasketDBO;
import com.nelolik.base_shop.basket_service.dto.BasketItemDTO;
import com.nelolik.base_shop.basket_service.model.basket.BasketItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BasketItemMapper {

    BasketItem basketItemDboYoBasketItem(BasketItemDTO itemDTO);

    BasketItemDTO basketItemToBasketItemDto(BasketItem item);

    @Mapping(target = "product.id", source = "productId")
    @Mapping(target = "product.name", source = "productName")
    @Mapping(target = "product.price", source = "productPrice")
    @Mapping(target = "quantity", source = "quantity")
    BasketItem basketDboToBasketItem(BasketDBO basketDBO);


    @Mapping(target = "productId", source = "item.product.id")
    @Mapping(target = "productName", source = "item.product.name")
    @Mapping(target = "productPrice", source = "item.product.price")
    BasketDBO basketItemToBasketDBO(BasketItem item, long basketId);
}
