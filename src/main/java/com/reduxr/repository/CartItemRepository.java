package com.reduxr.repository;

import com.reduxr.model.CartItem;
import com.reduxr.model.ShoppingCart;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Set<CartItem> getCartItemsByShoppingCart(ShoppingCart shoppingCart);
}
