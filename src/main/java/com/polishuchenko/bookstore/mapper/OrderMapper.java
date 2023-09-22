package com.polishuchenko.bookstore.mapper;

import com.polishuchenko.bookstore.config.MapperConfiguration;
import com.polishuchenko.bookstore.dto.order.OrderResponseDto;
import com.polishuchenko.bookstore.model.Order;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfiguration.class)
public interface OrderMapper {
    OrderResponseDto toDto(Order order);

    @AfterMapping
    default void initializeUserId(@MappingTarget OrderResponseDto responseDto, Order order) {
        responseDto.setUserId(order.getUser().getId());
    }
}
