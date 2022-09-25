package com.nelolik.base_shop.basket_service.model.basket;

import com.nelolik.base_shop.basket_service.dto.BasketDBO;
import com.nelolik.base_shop.basket_service.dto.mapper.BasketItemMapper;
import com.nelolik.base_shop.basket_service.dto.mapper.BasketItemMapperImpl;
import com.nelolik.base_shop.basket_service.model.product.ProductShort;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Basket {

    private Long basketId;

    private List<BasketItem> items;

    private static final  BasketItemMapper itemMapper = new BasketItemMapperImpl();

    public BigDecimal getTotalPrice() {
        BigDecimal price = BigDecimal.ZERO;
        for (BasketItem item :
                items) {
            price = price.add(item.getPrice());
        }
        return price;
    }

    public void addProduct(ProductShort product, int quantity) {
        for (BasketItem item :
                items) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.increaseQuantity(quantity);
                return;
            }
        }
    }

    public boolean removeProductFromBasket(BasketDb db, long productId, long basketId) {
        for (BasketItem item :
                items) {
            if (item.getProduct().getId() == productId) {
                db.deleteItemById(productId, basketId);
                return items.remove(item);
            }
        }
        return false;
    }

    public List<ProductShort> getProductList() {
        return items.stream().map(BasketItem::getProduct).collect(Collectors.toList());
    }

    public List<BasketItem> getItemsList() {
        return items;
    }

    public void setProductQuantity(long productId, int quantity) {
        for (BasketItem item :
                items) {
            if (item.getProduct().getId() == productId) {
                item.setQuantity(quantity);
                return;
            }
        }
    }

    public void saveBasket(BasketDb db) {
        List<BasketDBO> dbos = new ArrayList<>();
        for (BasketItem item :
                items) {
            ProductShort p = item.getProduct();
            dbos.add(itemMapper.basketItemToBasketDBO(item, basketId));
        }
        db.saveBasket(dbos);
    }

    public void updateBasket(BasketDb db) {
        for (BasketItem item :
                items) {
            ProductShort p = item.getProduct();
            db.updateBasket(itemMapper.basketItemToBasketDBO(item, basketId));
        }
    }

    public static Basket getBasketById(BasketDb db, long basketId) {
        List<BasketDBO> basketDBOs = db.getBasketItemsById(basketId);
        if (basketDBOs == null || basketDBOs.isEmpty()) {
            return new Basket(basketId, Collections.emptyList());
        }
        List<BasketItem> itemList = basketDBOs.stream().map(itemMapper::basketDboToBasketItem)
                .collect(Collectors.toList());
        return new Basket(basketId, itemList);
    }

    public static void deleteBasket(BasketDb db, long basketId) {
        db.deleteBasket(basketId);
    }
}
