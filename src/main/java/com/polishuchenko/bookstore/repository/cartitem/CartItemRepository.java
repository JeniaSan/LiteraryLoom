package com.polishuchenko.bookstore.repository.cartitem;

import com.polishuchenko.bookstore.model.CartItem;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findCartItemByIdAndShoppingCartId(Long cartItemId, Long shoppingCartId);

    Set<CartItem> getCartItemsByShoppingCartId(Long id);
}
