package com.reduxr.controller;

import com.reduxr.dto.cart.CreateCartItemRequestDto;
import com.reduxr.dto.cart.ShoppingCartDto;
import com.reduxr.dto.cart.UpdateCartItemRequestDto;
import com.reduxr.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management", description = "User endpoints for managing shopping cart")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class CartController {
    private final ShoppingCartService shoppingCartService;
    
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get shopping cart", description = "Get shopping cart " 
            + "of currently logged user")
    @GetMapping
    public ShoppingCartDto getShoppingCart() {
        return shoppingCartService.getShoppingCart();
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Add cart item", description = "Create new cart item in shopping cart")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCartDto addItemToCart(@RequestBody @Valid CreateCartItemRequestDto requestDto) {
        return shoppingCartService.saveCartItem(requestDto);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Update item quantity", description = "Update cart item quantity by id")
    @PutMapping("/items/{cartItemId}")
    public ShoppingCartDto updateItemQuantityInCart(
            @PathVariable Long cartItemId, 
            @RequestBody @Valid UpdateCartItemRequestDto requestDto) {
        return shoppingCartService.updateCartItem(cartItemId, requestDto);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Delete item", description = "Delete item from shopping cart by id")
    @DeleteMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItemFromCart(@PathVariable Long cartItemId) {
        shoppingCartService.deleteCartItem(cartItemId);
    }
}
