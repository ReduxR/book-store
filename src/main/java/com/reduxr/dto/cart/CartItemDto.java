package com.reduxr.dto.cart;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder("id")
public record CartItemDto(Long id, Long bookId, String bookTitle, int quantity) {
}
