package com.reduxr.service;

import com.reduxr.dto.cart.CartItemDto;
import com.reduxr.dto.cart.CreateCartItemRequestDto;
import com.reduxr.dto.cart.ShoppingCartDto;
import com.reduxr.dto.cart.UpdateCartItemRequestDto;

public interface ShoppingCartService {
    CartItemDto saveCartItem(CreateCartItemRequestDto requestDto);
    
    ShoppingCartDto getShoppingCart();
    
    CartItemDto updateCartItem(Long id, UpdateCartItemRequestDto requestDto);
    
    void deleteCartItem(Long id);
}
