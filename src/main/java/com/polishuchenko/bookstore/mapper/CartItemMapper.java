package com.polishuchenko.bookstore.mapper;

import com.polishuchenko.bookstore.config.MapperConfiguration;
import com.polishuchenko.bookstore.dto.cartitem.CartItemResponseDto;
import com.polishuchenko.bookstore.model.CartItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfiguration.class)
public interface CartItemMapper {
    CartItemResponseDto toDto(CartItem cartItem);

    @AfterMapping
    default void initialize(@MappingTarget CartItemResponseDto responseDto, CartItem cartItem) {
        responseDto.setId(cartItem.getId());
        responseDto.setBookId(cartItem.getBook().getId());
        responseDto.setBookTitle(cartItem.getBook().getTitle());
    }
}
