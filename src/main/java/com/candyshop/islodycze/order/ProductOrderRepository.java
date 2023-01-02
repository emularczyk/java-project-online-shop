package com.candyshop.islodycze.order;

import com.candyshop.islodycze.model.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {

    List<ProductOrder> findAllByOrdersOrderId(Long orderId);
}
