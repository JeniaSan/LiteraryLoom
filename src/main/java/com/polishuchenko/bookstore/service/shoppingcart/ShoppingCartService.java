package com.polishuchenko.bookstore.service.shoppingcart;

import com.polishuchenko.bookstore.dto.cartitem.CartItemRequestDto;
import com.polishuchenko.bookstore.dto.cartitem.CartItemResponseDto;
import com.polishuchenko.bookstore.dto.cartitem.UpdateCartItemDto;
import com.polishuchenko.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.polishuchenko.bookstore.model.ShoppingCart;
import com.polishuchenko.bookstore.model.User;
import org.springframework.security.core.Authentication;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(Authentication authentication);

    CartItemResponseDto addCartItem(CartItemRequestDto cartItemRequestDto,
                                    Authentication authentication);

    CartItemResponseDto update(Long id, UpdateCartItemDto updateCartItemDto);

    void delete(Long id);

    void clear(ShoppingCart shoppingCart, Authentication authentication);

    ShoppingCart getCurrentUserCart(User user);

    void createShoppingCart(User savedUser);
}
