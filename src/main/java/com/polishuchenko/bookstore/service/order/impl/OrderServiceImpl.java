package com.polishuchenko.bookstore.service.order.impl;

import com.polishuchenko.bookstore.dto.order.OrderAddressDto;
import com.polishuchenko.bookstore.dto.order.OrderResponseDto;
import com.polishuchenko.bookstore.dto.order.OrderStatusDto;
import com.polishuchenko.bookstore.dto.orderitem.OrderItemDto;
import com.polishuchenko.bookstore.exception.EntityNotFoundException;
import com.polishuchenko.bookstore.mapper.OrderItemMapper;
import com.polishuchenko.bookstore.mapper.OrderMapper;
import com.polishuchenko.bookstore.mapper.ShoppingCartMapper;
import com.polishuchenko.bookstore.model.Order;
import com.polishuchenko.bookstore.model.ShoppingCart;
import com.polishuchenko.bookstore.model.User;
import com.polishuchenko.bookstore.repository.order.OrderRepository;
import com.polishuchenko.bookstore.service.order.OrderService;
import com.polishuchenko.bookstore.service.shoppingcart.ShoppingCartService;
import com.polishuchenko.bookstore.service.user.UserService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ShoppingCartService shoppingCartService;
    private final ShoppingCartMapper shoppingCartMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public List<OrderResponseDto> getAllOrders() {
        User user = userService.getAuthenticatedUser();
        return orderRepository.getOrdersByUserId(user.getId()).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderResponseDto addAddress(OrderAddressDto request) {
        Order order = createOrder(request);
        order.setShippingAddress(request.getOrderAddress());
        shoppingCartService.clear(
                shoppingCartMapper.toEntity(shoppingCartService.getShoppingCart()));
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public Set<OrderItemDto> getAllOrderItems(Long orderId) {
        return allOrderItemsByOrderId(orderId)
                .collect(Collectors.toSet());
    }

    @Override
    public OrderItemDto getOrderItemById(Long orderId, Long itemId) {
        return allOrderItemsByOrderId(orderId)
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find item with id= " + itemId));
    }

    @Override
    public OrderResponseDto changeStatus(Long id, OrderStatusDto request) {
        Order order = orderRepository
                .getOrderByIdAndUserId(id, userService.getAuthenticatedUser().getId());
        order.setStatus(request.status());
        return orderMapper.toDto(orderRepository.save(order));
    }

    private Order createOrder(OrderAddressDto request) {
        Order order = new Order();
        ShoppingCart shoppingCart = shoppingCartService.getCurrentUserCart();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.Status.PENDING);
        order.setShippingAddress(request.getOrderAddress());
        order.setUser(userService.getAuthenticatedUser());
        order.setTotal(shoppingCart.getCartItems().stream()
                .map(i -> i.getBook().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal::add)
                .orElseThrow());
        order.setOrderItems(shoppingCart.getCartItems().stream()
                .map(orderItemMapper::cratItemToOrderItem)
                .collect(Collectors.toSet()));
        return orderRepository.save(order);
    }

    private Stream<OrderItemDto> allOrderItemsByOrderId(Long orderId) {
        return orderRepository.getOrderByIdAndUserId(orderId,
                        userService.getAuthenticatedUser().getId()).getOrderItems().stream()
                .map(orderItemMapper::toDto);
    }
}
