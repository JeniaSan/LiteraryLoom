package com.polishuchenko.bookstore.service.order;

import com.polishuchenko.bookstore.dto.order.OrderAddressDto;
import com.polishuchenko.bookstore.dto.order.OrderResponseDto;
import com.polishuchenko.bookstore.dto.order.OrderStatusDto;
import com.polishuchenko.bookstore.dto.orderitem.OrderItemDto;
import java.util.List;
import java.util.Set;
import org.springframework.security.core.Authentication;

public interface OrderService {
    List<OrderResponseDto> getAllOrders();

    OrderResponseDto addAddress(OrderAddressDto request, Authentication authentication);

    Set<OrderItemDto> getAllOrderItems(Long orderId);

    OrderItemDto getOrderItemById(Long orderId, Long itemId);

    OrderResponseDto changeStatus(Long id, OrderStatusDto request);
}


