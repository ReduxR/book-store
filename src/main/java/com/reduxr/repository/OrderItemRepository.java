package com.reduxr.repository;

import com.reduxr.model.OrderItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByOrderIdAndId(Long orderId, Long id);
    
    List<OrderItem> findByOrderId(Long orderId);
}
