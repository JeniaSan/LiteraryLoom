package com.polishuchenko.bookstore.mapper;

import com.polishuchenko.bookstore.config.MapperConfiguration;
import com.polishuchenko.bookstore.dto.cartitem.CartItemResponseDto;
import com.polishuchenko.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.polishuchenko.bookstore.model.CartItem;
import com.polishuchenko.bookstore.model.ShoppingCart;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfiguration.class)
public interface ShoppingCartMapper {
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @AfterMapping
    default void setUserId(@MappingTarget ShoppingCartDto cartDto, ShoppingCart shoppingCart) {
        cartDto.setUserId(shoppingCart.getUser().getId());
    }

    @AfterMapping
    default void cartItemsToDto(
            @MappingTarget ShoppingCartDto cartDto, ShoppingCart shoppingCart) {
        Set<CartItemResponseDto> itemsDtos = shoppingCart.getCartItems().stream()
                .map(this::mapToDto)
                .collect(Collectors.toSet());
        cartDto.setCartItems(itemsDtos);
    }

    default CartItemResponseDto mapToDto(CartItem cartItem) {
        CartItemResponseDto responseDto = new CartItemResponseDto();
        responseDto.setId(cartItem.getId());
        responseDto.setBookId(cartItem.getBook().getId());
        responseDto.setBookTitle(cartItem.getBook().getTitle());
        responseDto.setQuantity(cartItem.getQuantity());
        return responseDto;
    }
}
