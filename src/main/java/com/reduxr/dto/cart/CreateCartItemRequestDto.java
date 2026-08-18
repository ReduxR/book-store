package com.reduxr.dto.cart;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CreateCartItemRequestDto {
    private Long bookId;
    
    @Min(1)
    private Integer quantity;
}
