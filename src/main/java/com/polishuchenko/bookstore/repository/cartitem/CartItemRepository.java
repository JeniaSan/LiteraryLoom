package com.polishuchenko.bookstore.repository.cartitem;

import com.polishuchenko.bookstore.model.CartItem;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findCartItemById(Long cartItemId);

    @Query(value = "SELECT * FROM cart_item c WHERE c.shopping_cart_id = :id", nativeQuery = true)
    Set<CartItem> getCartItemsByShoppingCartId(Long id);
}
