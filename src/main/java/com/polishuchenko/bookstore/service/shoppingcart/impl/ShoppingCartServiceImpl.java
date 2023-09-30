package com.polishuchenko.bookstore.service.shoppingcart.impl;

import com.polishuchenko.bookstore.dto.cartitem.CartItemRequestDto;
import com.polishuchenko.bookstore.dto.cartitem.CartItemResponseDto;
import com.polishuchenko.bookstore.dto.cartitem.UpdateCartItemDto;
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
import com.polishuchenko.bookstore.repository.user.UserRepository;
import com.polishuchenko.bookstore.service.shoppingcart.ShoppingCartService;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartMapper shoppingCartMapper;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public CartItemResponseDto update(Long id, UpdateCartItemDto updateCartItemDto) {
        CartItem cartItem = cartItemRepository
                .findCartItemById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find item with id= " + id));
        cartItem.setQuantity(updateCartItemDto.getQuantity());
        cartItem.setId(id);
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void delete(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public void clear(ShoppingCart shoppingCart, Authentication authentication) {
        cartItemRepository.deleteAll(shoppingCart.getCartItems());
        User user = (User) authentication.getPrincipal();
        getCurrentUserCart(user).getCartItems().clear();
    }

    @Override
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        String userName = authentication.getName();
        User currentUser = userRepository.findUserByUsername(userName);
        ShoppingCart shoppingCart = getCurrentUserCart(currentUser);
        ShoppingCartDto shoppingCartDto = shoppingCartMapper.toDto(shoppingCart);
        shoppingCartDto.setCartItems(shoppingCart.getCartItems().stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toSet()));
        return shoppingCartDto;
    }

    @Transactional
    @Override
    public CartItemResponseDto addCartItem(CartItemRequestDto cartItemRequestDto,
                                           Authentication authentication) {
        String userName = authentication.getName();
        User currentUser = userRepository.findUserByUsername(userName);
        ShoppingCart shoppingCart = getCurrentUserCart(currentUser);
        Book book = bookRepository.findById(cartItemRequestDto.bookId()).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find book with id=" + cartItemRequestDto.bookId()));
        CartItem cartItem = getCartItem(shoppingCart, book);
        cartItem.setQuantity(cartItemRequestDto.quantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public ShoppingCart getCurrentUserCart(User user) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUserId(user.getId());
        shoppingCart.setCartItems(
                cartItemRepository.getCartItemsByShoppingCartId(shoppingCart.getId()));
        return shoppingCart;
    }

    @Override
    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    private CartItem getCartItem(ShoppingCart shoppingCart, Book book) {
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
        return cartItem;
    }
}
