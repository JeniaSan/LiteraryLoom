package com.polishuchenko.bookstore.controller;

import com.polishuchenko.bookstore.dto.order.OrderAddressDto;
import com.polishuchenko.bookstore.dto.order.OrderResponseDto;
import com.polishuchenko.bookstore.dto.order.OrderStatusDto;
import com.polishuchenko.bookstore.dto.orderitem.OrderItemDto;
import com.polishuchenko.bookstore.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Order management", description = "Endpoints for managing orders")
@RestController
@RequestMapping(value = "/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Get all orders", description = "Get orders history")
    @GetMapping
    public List<OrderResponseDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    @Operation(summary = "Create an order", description = "Create an order and clear shopping cart")
    @PostMapping
    public OrderResponseDto addShippingAddress(@RequestBody @Valid OrderAddressDto request) {
        return orderService.addAddress(request);
    }

    @Operation(summary = "Get all items from order",
            description = "Get a list of all items from order")
    @GetMapping("/{orderId}/items")
    public Set<OrderItemDto> getAllOrderItems(@PathVariable Long orderId) {
        return orderService.getAllOrderItems(orderId);
    }

    @Operation(summary = "Get an order item by id", description = "Get an order item from order")
    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemDto getOrderItemById(@PathVariable Long orderId, @PathVariable Long itemId) {
        return orderService.getOrderItemById(orderId, itemId);
    }

    @Operation(summary = "Change status", description = "Change order status")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public OrderResponseDto changeStatus(@PathVariable Long id,
                                         @RequestBody @Valid OrderStatusDto request) {
        return orderService.changeStatus(id, request);
    }
}
