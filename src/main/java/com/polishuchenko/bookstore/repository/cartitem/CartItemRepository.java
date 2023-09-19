package com.polishuchenko.bookstore.repository.cartitem;

import com.polishuchenko.bookstore.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findCartItemByIdAndShoppingCartId(Long cartItemId, Long shoppingCartId);
}
