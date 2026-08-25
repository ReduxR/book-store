package com.reduxr.controller;

import com.reduxr.dto.order.CreateOrderRequestDto;
import com.reduxr.dto.order.OrderDto;
import com.reduxr.dto.order.OrderItemDto;
import com.reduxr.dto.order.UpdateOrderStatusRequestDto;
import com.reduxr.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for managing user orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    
    @Operation(summary = "Get all user orders")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public Page<OrderDto> getOrders(@ParameterObject Pageable pageable) {
        return orderService.getCurrentUserOrders(pageable);
    }
    
    @Operation(summary = "Get order items by order id")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{orderId}/items")
    public List<OrderItemDto> getOrderItems(@PathVariable Long orderId) {
        return orderService.getOrderItems(orderId);
    }
    
    @Operation(summary = "Get order item by order id and id")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{orderId}/items/{id}")
    public OrderItemDto getOrderItem(@PathVariable Long orderId, 
                                     @PathVariable Long id) {
        return orderService.getOrderItem(orderId, id);
    }
    
    @Operation(summary = "Create new order")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping
    public OrderDto createOrder(@RequestBody @Valid CreateOrderRequestDto requestDto) {
        return orderService.createOrder(requestDto);
    }
    
    @Operation(summary = "Update order delivery status")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public OrderDto updateOrderStatus(@RequestBody @Valid UpdateOrderStatusRequestDto requestDto,
                                      @PathVariable Long id) {
        return orderService.updateOrderStatus(id, requestDto);
    }
}
