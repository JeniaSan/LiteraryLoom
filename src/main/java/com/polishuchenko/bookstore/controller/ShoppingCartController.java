package com.polishuchenko.bookstore.controller;

import com.polishuchenko.bookstore.dto.cartitem.CartItemRequestDto;
import com.polishuchenko.bookstore.dto.cartitem.CartItemResponseDto;
import com.polishuchenko.bookstore.dto.cartitem.UpdateCartItemDto;
import com.polishuchenko.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.polishuchenko.bookstore.service.cartitem.CartItemService;
import com.polishuchenko.bookstore.service.shoppingcart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Cart management", description = "Endpoints for managing cart and cart items")
@RestController
@RequestMapping(value = "/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final CartItemService cartItemService;

    @Operation(summary = "Get a shopping cart",
            description = "Get a shopping cart with all cart items")
    @GetMapping
    public ShoppingCartDto getShoppingCart() {
        return shoppingCartService.getShoppingCart();
    }

    @Operation(summary = "Add a new cart item",
            description = "Add a new cart item into shopping cart")
    @PostMapping
    public CartItemResponseDto addCartItem(@RequestBody @Valid CartItemRequestDto cartItemDto) {
        return shoppingCartService.addCartItem(cartItemDto);
    }

    @Operation(summary = "Update a cart item", description = "Update a cart item in shopping cart")
    @PutMapping("/cart-items/{cartItemId}")
    public CartItemResponseDto update(
            @PathVariable Long cartItemId, @RequestBody @Valid UpdateCartItemDto updateDto) {
        return cartItemService.update(cartItemId, updateDto);
    }

    @Operation(summary = "Delete cart item", description = "Delete cart item in shopping cart")
    @DeleteMapping("/cart-items/{cartItemId}")
    public void delete(@PathVariable Long cartItemId) {
        cartItemService.delete(cartItemId);
    }
}
