package com.reduxr.mapper;

import com.reduxr.config.MapperConfig;
import com.reduxr.dto.cart.CartItemDto;
import com.reduxr.dto.cart.CreateCartItemRequestDto;
import com.reduxr.dto.cart.UpdateCartItemRequestDto;
import com.reduxr.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemDto toDto(CartItem cartItem);
    
    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    CartItem toModel(CreateCartItemRequestDto requestDto);
    
    void updateCartItemFromDto(UpdateCartItemRequestDto requestDto, 
                               @MappingTarget CartItem cartItem);
}
