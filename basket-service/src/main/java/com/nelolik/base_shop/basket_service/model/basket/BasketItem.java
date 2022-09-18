package com.nelolik.base_shop.basket_service.model.basket;

import com.nelolik.base_shop.basket_service.model.product.ProductShort;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
public class BasketItem {

    private ProductShort product;

    private int quantity;

    public BigDecimal getPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public void increaseQuantity(int quantity) {
        if (quantity >= 0) {
            this.quantity += quantity;
        }
    }
}
