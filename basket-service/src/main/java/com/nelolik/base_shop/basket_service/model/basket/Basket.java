package com.nelolik.base_shop.basket_service.model.basket;

import com.nelolik.base_shop.basket_service.model.dto.BasketDBO;
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

    public boolean removeProductFromBasket(BasketDb db, long productId) {
        for (BasketItem item :
                items) {
            if (item.getProduct().getId() == productId) {
                db.deleteItemById(productId);
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
            dbos.add(new BasketDBO(basketId, p.getId(), p.getName(), p.getPrice(), item.getQuantity()));
        }
        db.saveBasket(dbos);
    }

    public void updateBasket(BasketDb db) {
        for (BasketItem item :
                items) {
            ProductShort p = item.getProduct();
            db.updateBasket(new BasketDBO(basketId, p.getId(), p.getName(), p.getPrice(), item.getQuantity()));
        }
    }

    public static Basket getBasketById(BasketDb db, long basketId) {
        List<BasketDBO> basketDBOs = db.getBasketItemsById(basketId);
        if (basketDBOs == null || basketDBOs.isEmpty()) {
            return new Basket(basketId, Collections.emptyList());
        }
        List<BasketItem> itemList = basketDBOs.stream().map(dbo ->
                        new BasketItem(
                                new ProductShort(dbo.getProductId(), dbo.getProductName(), dbo.getProductPrice()),
                                dbo.getQuantity()))
                .collect(Collectors.toList());
        return new Basket(basketId, itemList);
    }

    public static void deleteBasket(BasketDb db, long basketId) {
        db.deleteBasket(basketId);
    }
}
