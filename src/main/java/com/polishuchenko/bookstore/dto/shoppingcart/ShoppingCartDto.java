package com.polishuchenko.bookstore.dto.shoppingcart;

import com.polishuchenko.bookstore.dto.cartitem.CartItemResponseDto;
import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}
