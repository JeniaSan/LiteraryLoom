package com.polishuchenko.bookstore.mapper;

import com.polishuchenko.bookstore.config.MapperConfiguration;
import com.polishuchenko.bookstore.dto.orderitem.OrderItemDto;
import com.polishuchenko.bookstore.model.CartItem;
import com.polishuchenko.bookstore.model.OrderItem;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface OrderItemMapper {
    OrderItem cratItemToOrderItem(CartItem cartItem);

    OrderItemDto toDto(OrderItem orderItem);
}
