package com.polishuchenko.bookstore.service.shoppingcart.impl;

import com.polishuchenko.bookstore.dto.cartitem.CartItemRequestDto;
import com.polishuchenko.bookstore.dto.cartitem.CartItemResponseDto;
import com.polishuchenko.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.polishuchenko.bookstore.exception.EntityNotFoundException;
import com.polishuchenko.bookstore.mapper.CartItemMapper;
import com.polishuchenko.bookstore.mapper.ShoppingCartMapper;
import com.polishuchenko.bookstore.model.Book;
import com.polishuchenko.bookstore.model.CartItem;
import com.polishuchenko.bookstore.model.ShoppingCart;
import com.polishuchenko.bookstore.model.User;
import com.polishuchenko.bookstore.repository.book.BookRepository;
import com.polishuchenko.bookstore.repository.cartitem.CartItemRepository;
import com.polishuchenko.bookstore.repository.shoppingcart.ShoppingCartRepository;
import com.polishuchenko.bookstore.service.shoppingcart.ShoppingCartService;
import com.polishuchenko.bookstore.service.user.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserService userService;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartMapper shoppingCartMapper;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartDto getShoppingCart() {
        return shoppingCartMapper.toDto(currentUserCart());
    }

    @Transactional
    @Override
    public CartItemResponseDto addCartItem(CartItemRequestDto cartItemRequestDto) {
        ShoppingCart shoppingCart = currentUserCart();
        Book book = bookRepository.findById(cartItemRequestDto.bookId()).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find book with id=" + cartItemRequestDto.bookId()));
        Optional<CartItem> cartItemOptional = shoppingCart.getCartItems().stream()
                .filter(i -> i.getBook().equals(book))
                .findFirst();
        CartItem cartItem;
        if (cartItemOptional.isPresent()) {
            cartItem = cartItemOptional.get();
        } else {
            cartItem = new CartItem();
            cartItem.setShoppingCart(shoppingCart);
            cartItem.setBook(book);
        }
        cartItem.setQuantity(cartItemRequestDto.quantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    private ShoppingCart currentUserCart() {
        User user = userService.getAuthenticatedUser();
        return shoppingCartRepository.findShoppingCartByUserId(user.getId());
    }
}
