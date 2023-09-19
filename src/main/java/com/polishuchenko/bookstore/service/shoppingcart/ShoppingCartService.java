package com.polishuchenko.bookstore.service.shoppingcart;

import com.polishuchenko.bookstore.dto.cartitem.CartItemRequestDto;
import com.polishuchenko.bookstore.dto.cartitem.CartItemResponseDto;
import com.polishuchenko.bookstore.dto.cartitem.UpdateCartItemDto;
import com.polishuchenko.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.polishuchenko.bookstore.model.ShoppingCart;
import com.polishuchenko.bookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart();

    CartItemResponseDto addCartItem(CartItemRequestDto cartItemRequestDto);

    CartItemResponseDto update(Long id, UpdateCartItemDto updateCartItemDto);

    void delete(Long id);

    void clear(ShoppingCart shoppingCart);

    ShoppingCart getCurrentUserCart();

    void createShoppingCart(User savedUser);
}
