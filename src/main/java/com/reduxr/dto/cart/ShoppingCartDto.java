package com.reduxr.dto.cart;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonPropertyOrder("id")
public record ShoppingCartDto(Long id, Long userId, List<CartItemDto> cartItems) {
    
}
