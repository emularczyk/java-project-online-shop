package com.candyshop.islodycze.shopping_cart;

import com.candyshop.islodycze.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    List<CartItem> findAllByUserEmail(String username);

    void deleteByUserEmailAndProductProductId(String username, Long productId);

    void deleteAllByUserUserId(Long userId);

    Set<CartItem> findAllByUserUserId(Long userId);

    CartItem findByUserUserIdAndProductProductId(Long userId, Long productId);
}
