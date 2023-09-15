package com.polishuchenko.bookstore.service.shoppingcart;

import com.polishuchenko.bookstore.dto.cartitem.CartItemRequestDto;
import com.polishuchenko.bookstore.dto.cartitem.CartItemResponseDto;
import com.polishuchenko.bookstore.dto.shoppingcart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart();

    CartItemResponseDto addCartItem(CartItemRequestDto cartItemRequestDto);
}
