package com.polishuchenko.bookstore.mapper;

import com.polishuchenko.bookstore.config.MapperConfiguration;
import com.polishuchenko.bookstore.dto.orderitem.OrderItemDto;
import com.polishuchenko.bookstore.model.CartItem;
import com.polishuchenko.bookstore.model.Order;
import com.polishuchenko.bookstore.model.OrderItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfiguration.class)
public interface OrderItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    OrderItem cartItemToOrderItem(CartItem cartItem, Order order);

    @AfterMapping
    default void initializeOrderInItem(@MappingTarget
                                       OrderItem orderItem, CartItem cartItem, Order order) {
        orderItem.setOrder(order);
        orderItem.setBook(cartItem.getBook());
        orderItem.setPrice(cartItem.getBook().getPrice());
    }

    OrderItemDto toDto(OrderItem orderItem);

    @AfterMapping
    default void initializeBookId(@MappingTarget OrderItemDto orderItemDto, OrderItem orderItem) {
        orderItemDto.setBookId(orderItem.getBook().getId());
    }
}
