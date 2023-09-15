package com.polishuchenko.bookstore.dto.cartitem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemRequestDto(@NotNull @Min(value = 1) Long bookId,
                                 @NotNull @Min(value = 1) int quantity) {}
