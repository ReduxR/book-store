package com.reduxr.service;

import com.reduxr.dto.order.CreateOrderRequestDto;
import com.reduxr.dto.order.OrderDto;
import com.reduxr.dto.order.OrderItemDto;
import com.reduxr.dto.order.UpdateOrderStatusRequestDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderDto> getCurrentUserOrders(Pageable pageable);
    
    OrderItemDto getOrderItem(Long orderId, Long id);
    
    List<OrderItemDto> getOrderItems(Long orderId);
    
    OrderDto createOrder(CreateOrderRequestDto requestDto);
    
    OrderDto updateOrderStatus(Long id, UpdateOrderStatusRequestDto requestDto);
}
