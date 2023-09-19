package com.polishuchenko.bookstore.repository.order;

import com.polishuchenko.bookstore.model.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order getOrderByIdAndUserId(Long orderId, Long userId);

    List<Order> getOrdersByUserId(Long userId);
}
