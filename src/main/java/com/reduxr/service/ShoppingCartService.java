package com.reduxr.service;

import com.reduxr.dto.cart.CreateCartItemRequestDto;
import com.reduxr.dto.cart.ShoppingCartDto;
import com.reduxr.dto.cart.UpdateCartItemRequestDto;
import com.reduxr.model.User;

public interface ShoppingCartService {
    void setShoppingCartToUser(User user);
            
    ShoppingCartDto saveCartItem(CreateCartItemRequestDto requestDto);
    
    ShoppingCartDto getShoppingCart();
    
    ShoppingCartDto updateCartItem(Long id, UpdateCartItemRequestDto requestDto);
    
    void deleteCartItem(Long id);
}
