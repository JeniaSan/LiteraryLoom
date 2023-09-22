package com.polishuchenko.bookstore.repository.orderitem;

import com.polishuchenko.bookstore.model.OrderItem;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Set<OrderItem> getAllByOrderId(Long id);
}
