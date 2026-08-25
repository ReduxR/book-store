package com.reduxr.service.impl;

import com.reduxr.dto.order.CreateOrderRequestDto;
import com.reduxr.dto.order.OrderDto;
import com.reduxr.dto.order.OrderItemDto;
import com.reduxr.dto.order.UpdateOrderStatusRequestDto;
import com.reduxr.exception.EntityNotFoundException;
import com.reduxr.mapper.OrderItemMapper;
import com.reduxr.mapper.OrderMapper;
import com.reduxr.model.Order;
import com.reduxr.model.OrderItem;
import com.reduxr.model.ShoppingCart;
import com.reduxr.model.Status;
import com.reduxr.model.User;
import com.reduxr.repository.OrderItemRepository;
import com.reduxr.repository.OrderRepository;
import com.reduxr.repository.ShoppingCartRepository;
import com.reduxr.service.OrderService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;
    
    @Override
    public Page<OrderDto> getCurrentUserOrders(Pageable pageable) {
        return orderRepository.findAllByUserId(getUserId(), pageable)
                .map(orderMapper::toDto);
    }
    
    @Override
    public OrderItemDto getOrderItem(Long orderId, Long id) {
        OrderItem orderItem = orderItemRepository.findByOrderIdAndId(orderId, id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Order with order id: %s and id: %s not found", orderId, id))
                );
        return orderItemMapper.toDto(orderItem);
    }
    
    @Override
    public List<OrderItemDto> getOrderItems(Long orderId) {
        return orderItemRepository.findByOrderId(orderId).stream()
                .map(orderItemMapper::toDto)
                .toList();
    }
    
    @Override
    public OrderDto createOrder(CreateOrderRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart"
                        + " not found for user with id: " + getUserId()));
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Shopping cart with id: "
                    + shoppingCart.getId() + " is empty");
        }
        
        Set<OrderItem> orderItems = orderItemMapper.toOrderItems(shoppingCart.getCartItems());
        Order order = buildOrder(shoppingCart.getUser(), 
                orderItems, requestDto.getShippingAddress());
        
        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }
    
    public OrderDto updateOrderStatus(Long id, UpdateOrderStatusRequestDto requestDto) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Order with id: " + id + " not found")
        );
        
        orderMapper.updateModelFromDto(requestDto, order);
        order.setStatus(requestDto.getStatus());
        Order saved = orderRepository.save(order);
        return orderMapper.toDto(saved);
    }
    
    private Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
    
    private Order buildOrder(User user, 
                             Set<OrderItem> orderItems, 
                             String shippingAddress) {
        Order order = new Order();
        order.setUser(user);
        order.setStatus(Status.PENDING);
        order.setTotal(calculateTotal(orderItems));
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(shippingAddress);
        order.setOrderItems(orderItems);
        
        orderItems.forEach(orderItem -> orderItem.setOrder(order));
        return order;
    }
    
    private BigDecimal calculateTotal(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
