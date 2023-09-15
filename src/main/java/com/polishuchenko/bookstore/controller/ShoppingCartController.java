package com.polishuchenko.bookstore.controller;

import com.polishuchenko.bookstore.dto.cartitem.CartItemRequestDto;
import com.polishuchenko.bookstore.dto.cartitem.CartItemResponseDto;
import com.polishuchenko.bookstore.dto.cartitem.UpdateCartItemDto;
import com.polishuchenko.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.polishuchenko.bookstore.service.cartitem.CartItemService;
import com.polishuchenko.bookstore.service.shoppingcart.ShoppingCartService;
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

@RestController
@RequestMapping(value = "/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final CartItemService cartItemService;

    @GetMapping
    public ShoppingCartDto getShoppingCart() {
        return shoppingCartService.getShoppingCart();
    }

    @PostMapping
    public CartItemResponseDto addCartItem(@RequestBody @Valid CartItemRequestDto cartItemDto) {
        return shoppingCartService.addCartItem(cartItemDto);
    }

    @PutMapping("/cart-items/{cartItemId}")
    public CartItemResponseDto update(
            @PathVariable Long cartItemId, @RequestBody @Valid UpdateCartItemDto updateDto) {
        return cartItemService.update(cartItemId, updateDto);
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    public void delete(@PathVariable Long cartItemId) {
        cartItemService.delete(cartItemId);
    }
}
