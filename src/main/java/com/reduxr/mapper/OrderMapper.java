package com.reduxr.mapper;

import com.reduxr.config.MapperConfig;
import com.reduxr.dto.order.OrderDto;
import com.reduxr.dto.order.UpdateOrderStatusRequestDto;
import com.reduxr.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    OrderDto toDto(Order order);
    
    Order toModel(OrderDto orderDto);
    
    void updateModelFromDto(UpdateOrderStatusRequestDto requestDto, @MappingTarget Order order);
}
