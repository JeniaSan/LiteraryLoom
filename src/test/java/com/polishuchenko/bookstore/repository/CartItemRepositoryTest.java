package com.polishuchenko.bookstore.repository;

import com.polishuchenko.bookstore.model.Book;
import com.polishuchenko.bookstore.model.CartItem;
import com.polishuchenko.bookstore.model.ShoppingCart;
import com.polishuchenko.bookstore.repository.cartitem.CartItemRepository;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartItemRepositoryTest {
    private static final Long VALID_ID = 1L;
    private static final int EXPECTED_SIZE = 1;
    private static final int DEFAULT_QUANTITY = 10;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Test
    @DisplayName("Get all cart items by shopping cart id")
    public void findAllCartItems_validShoppingCartId_returnsItemsList() {
        CartItem cartItem = new CartItem();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(VALID_ID);
        Book book = new Book();
        book.setId(VALID_ID);

        cartItem.setShoppingCart(shoppingCart);
        cartItem.setQuantity(DEFAULT_QUANTITY);
        cartItem.setBook(book);
        cartItemRepository.save(cartItem);

        Set<CartItem> actual = cartItemRepository.getCartItemsByShoppingCartId(1L);
        Assertions.assertEquals(actual.size(), EXPECTED_SIZE);
    }
}
