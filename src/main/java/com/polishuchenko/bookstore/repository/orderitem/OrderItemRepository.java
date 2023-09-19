package com.polishuchenko.bookstore.repository.orderitem;

import com.polishuchenko.bookstore.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    OrderItem getOrderItemById(Long id);
}
