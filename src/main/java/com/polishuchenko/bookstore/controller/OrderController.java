package com.polishuchenko.bookstore.controller;

import com.polishuchenko.bookstore.dto.order.OrderAddressDto;
import com.polishuchenko.bookstore.dto.order.OrderResponseDto;
import com.polishuchenko.bookstore.dto.order.OrderStatusDto;
import com.polishuchenko.bookstore.dto.orderitem.OrderItemDto;
import com.polishuchenko.bookstore.service.order.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public List<OrderResponseDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping
    public OrderResponseDto addShippingAddress(@RequestBody @Valid OrderAddressDto request) {
        return orderService.addAddress(request);
    }

    @GetMapping("/{orderId}/items")
    public Set<OrderItemDto> getAllOrderItems(@PathVariable Long orderId) {
        return orderService.getAllOrderItems(orderId);
    }

    @GetMapping("/orders/{orderId}/items/{itemId}")
    public OrderItemDto getOrderItemById(@PathVariable Long orderId, @PathVariable Long itemId) {
        return orderService.getOrderItemById(orderId, itemId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public OrderResponseDto changeStatus(@PathVariable Long id,
                                         @RequestBody @Valid OrderStatusDto request) {
        return orderService.changeStatus(id, request);
    }
}
