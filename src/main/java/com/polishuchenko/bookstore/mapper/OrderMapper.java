package com.polishuchenko.bookstore.mapper;

import com.polishuchenko.bookstore.config.MapperConfiguration;
import com.polishuchenko.bookstore.dto.order.OrderResponseDto;
import com.polishuchenko.bookstore.model.Order;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface OrderMapper {
    OrderResponseDto toDto(Order order);
}
