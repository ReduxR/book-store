package com.reduxr.controller;

import com.reduxr.dto.cart.CartItemDto;
import com.reduxr.dto.cart.CreateCartItemRequestDto;
import com.reduxr.dto.cart.ShoppingCartDto;
import com.reduxr.dto.cart.UpdateCartItemRequestDto;
import com.reduxr.service.ShoppingCartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class CartController {
    private final ShoppingCartService shoppingCartService;
    
    @GetMapping
    public ShoppingCartDto getShoppingCart() {
        return shoppingCartService.getShoppingCart();
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CartItemDto addItemToCart(@RequestBody @Valid CreateCartItemRequestDto requestDto) {
        return shoppingCartService.saveCartItem(requestDto);
    }
    
    @PutMapping("/items/{cartItemId}")
    public CartItemDto updateItemQuantityInCart(
            @PathVariable Long cartItemId, 
            @RequestBody @Valid UpdateCartItemRequestDto requestDto) {
        return shoppingCartService.updateCartItem(cartItemId, requestDto);
    }
    
    @DeleteMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItemFromCart(@PathVariable Long cartItemId) {
        shoppingCartService.deleteCartItem(cartItemId);
    }
}
