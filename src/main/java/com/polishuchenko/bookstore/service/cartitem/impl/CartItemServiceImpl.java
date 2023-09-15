package com.polishuchenko.bookstore.service.cartitem.impl;

import com.polishuchenko.bookstore.dto.cartitem.CartItemResponseDto;
import com.polishuchenko.bookstore.dto.cartitem.UpdateCartItemDto;
import com.polishuchenko.bookstore.exception.EntityNotFoundException;
import com.polishuchenko.bookstore.mapper.CartItemMapper;
import com.polishuchenko.bookstore.model.CartItem;
import com.polishuchenko.bookstore.repository.cartitem.CartItemRepository;
import com.polishuchenko.bookstore.service.cartitem.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public CartItemResponseDto update(Long id, UpdateCartItemDto updateCartItemDto) {
        CartItem cartItem = cartItemRepository.findCartItemById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find item with id= " + id));
        cartItem.setQuantity(updateCartItemDto.getQuantity());
        cartItem.setId(id);
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void delete(Long id) {
        cartItemRepository.deleteById(id);
    }
}
