package com.polishuchenko.bookstore.dto.order;

import com.polishuchenko.bookstore.model.Order;

public record OrderStatusDto(Order.Status status) {}
