package com.reduxr.mapper;

import com.reduxr.config.MapperConfig;
import com.reduxr.dto.cart.ShoppingCartDto;
import com.reduxr.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(target = "userId", source = "user.id")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);
    
    ShoppingCart toModel(ShoppingCartDto shoppingCartDto);
}
