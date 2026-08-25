package com.reduxr.mapper;

import com.reduxr.config.MapperConfig;
import com.reduxr.dto.order.OrderItemDto;
import com.reduxr.model.CartItem;
import com.reduxr.model.OrderItem;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface OrderItemMapper {
    @Mapping(target = "price", source = "book.price")
    @Mapping(target = "id", ignore = true)
    OrderItem toOrderItem(CartItem cartItem);
    
    Set<OrderItem> toOrderItems(Set<CartItem> cartItemDto);
    
    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    OrderItem toModel(OrderItemDto orderItemDto);
    
    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto toDto(OrderItem orderItem);
}
