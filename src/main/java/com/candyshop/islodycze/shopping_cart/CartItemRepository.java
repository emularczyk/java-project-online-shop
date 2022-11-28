package com.candyshop.islodycze.shopping_cart;

import com.candyshop.islodycze.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    List<CartItem> findAllByUserUserId(Long userId);

}
