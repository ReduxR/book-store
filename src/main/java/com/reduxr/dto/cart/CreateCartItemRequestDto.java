package com.reduxr.dto.cart;

import lombok.Data;

@Data
public class CreateCartItemRequestDto {
    private Long bookId;
    private Integer quantity;
}
