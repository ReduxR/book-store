package com.reduxr.dto.order;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.reduxr.model.Status;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

@Data
@JsonPropertyOrder("id")
public class OrderDto {
    private Long id;
    private Long userId;
    private Set<OrderItemDto> orderItems;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private Status status;
}
