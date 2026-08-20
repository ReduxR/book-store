package com.reduxr.service.impl;

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
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartMapper shoppingCartMapper;
    
    @Override
    public void setShoppingCartToUser(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }
    
    @Override
    public ShoppingCartDto saveCartItem(CreateCartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find " 
                        + "shopping cart by user id: " + getUserId()));
        
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        Optional<CartItem> cartItemOptional = cartItems.stream()
                .filter(cartItem -> cartItem.getBook().getId().equals(requestDto.getBookId()))
                .findFirst();
        
        cartItemOptional.ifPresentOrElse(
                cartItem -> cartItem.setQuantity(
                        cartItem.getQuantity() + requestDto.getQuantity()), 
                () -> {
                    CartItem cartItem = cartItemMapper.toModel(requestDto);
                    Book book = bookRepository.findById(requestDto.getBookId())
                            .orElseThrow(() -> new EntityNotFoundException("Book with id "
                                    + requestDto.getBookId() + " not found"));
                    cartItem.setBook(book);
                    cartItem.setShoppingCart(shoppingCart);
                    
                    shoppingCart.getCartItems().add(cartItem);
                });
        
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }
    
    @Override
    public ShoppingCartDto getShoppingCart() {
        return shoppingCartMapper.toDto(getCartOrThrow());
    }
    
    @Override
    public ShoppingCartDto updateCartItem(Long id, UpdateCartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = getCartOrThrow();
        CartItem cartItem = getCartItemOrThrow(id, shoppingCart.getId());
        
        cartItemMapper.updateCartItemFromDto(requestDto, cartItem);
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(shoppingCart);
    }
    
    @Override
    public void deleteCartItem(Long id) {
        ShoppingCart shoppingCart = getCartOrThrow();
        CartItem cartItem = getCartItemOrThrow(id, shoppingCart.getId());
        shoppingCart.getCartItems().remove(cartItem);
        shoppingCartRepository.save(shoppingCart);
    }
    
    private Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
    
    private CartItem getCartItemOrThrow(Long id, Long cartId) {
        return cartItemRepository
                .findByIdAndShoppingCartId(id, cartId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find "
                        + "cart item by id: " + id));
    }
    
    private ShoppingCart getCartOrThrow() {
        return shoppingCartRepository.findByUserId(getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart"
                        + " not found for user with id: " + getUserId()));
    }
}
