package com.reduxr.service.impl;

import com.reduxr.dto.cart.CartItemDto;
import com.reduxr.dto.cart.CreateCartItemRequestDto;
import com.reduxr.dto.cart.ShoppingCartDto;
import com.reduxr.dto.cart.UpdateCartItemRequestDto;
import com.reduxr.exception.EntityNotFoundException;
import com.reduxr.mapper.CartItemMapper;
import com.reduxr.mapper.ShoppingCartMapper;
import com.reduxr.model.Book;
import com.reduxr.model.CartItem;
import com.reduxr.model.ShoppingCart;
import com.reduxr.model.User;
import com.reduxr.repository.BookRepository;
import com.reduxr.repository.CartItemRepository;
import com.reduxr.repository.ShoppingCartRepository;
import com.reduxr.service.ShoppingCartService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartMapper shoppingCartMapper;
    
    @Override
    public CartItemDto saveCartItem(CreateCartItemRequestDto requestDto) {
        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book with id "
                        + requestDto.getBookId() + " not found"));
        
        CartItem cartItem = cartItemMapper.toModel(requestDto);
        cartItem.setBook(book);
        cartItem.setShoppingCart(getCartOrThrow());
                
        CartItem savedItem = cartItemRepository.save(cartItem);
        return cartItemMapper.toDto(savedItem);
    }
    
    @Override
    public ShoppingCartDto getShoppingCart() {
        ShoppingCart shoppingCart = getCartOrThrow();
        Set<CartItem> cartItemsByShoppingCart = cartItemRepository
                .getCartItemsByShoppingCart(shoppingCart);
        
        shoppingCart.setCartItems(cartItemsByShoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }
    
    @Override
    public CartItemDto updateCartItem(Long id, UpdateCartItemRequestDto requestDto) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cart item with id: "
                        + id + " not found"));
        cartItemMapper.updateCartItemFromDto(requestDto, cartItem);
        CartItem saved = cartItemRepository.save(cartItem);
        return cartItemMapper.toDto(saved);
    }
    
    @Override
    public void deleteCartItem(Long id) {
        if (!cartItemRepository.existsById(id)) {
            throw new EntityNotFoundException("Can't find cart item by id: " + id);
        }
        cartItemRepository.deleteById(id);
    }
    
    private Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
    
    private ShoppingCart getCartOrThrow() {
        return shoppingCartRepository.findByUserId(getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart"
                        + " not found for user with id: " + getUserId()));
    }
}
