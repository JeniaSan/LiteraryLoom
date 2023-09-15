package com.polishuchenko.bookstore.service.cartitem;

import com.polishuchenko.bookstore.dto.cartitem.CartItemResponseDto;
import com.polishuchenko.bookstore.dto.cartitem.UpdateCartItemDto;

public interface CartItemService {
    CartItemResponseDto update(Long id, UpdateCartItemDto updateCartItemDto);

    void delete(Long id);
}
