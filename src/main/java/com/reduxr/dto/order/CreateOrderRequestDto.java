package com.reduxr.dto.order;

import com.reduxr.validator.address.Address;
import lombok.Data;

@Data
public class CreateOrderRequestDto {
    @Address 
    private String shippingAddress;
}
