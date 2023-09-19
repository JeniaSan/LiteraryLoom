package com.polishuchenko.bookstore.dto.order;

import com.polishuchenko.bookstore.dto.orderitem.OrderItemDto;
import com.polishuchenko.bookstore.model.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private List<OrderItemDto> orderItems;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private Order.Status status;
}
