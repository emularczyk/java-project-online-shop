package com.candyshop.islodycze.order;

import com.candyshop.islodycze.mainPage.ProductRepository;
import com.candyshop.islodycze.model.Order;
import com.candyshop.islodycze.model.ProductOrder;
import com.candyshop.islodycze.model.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductOrderRepository productOrderRepository;

    public void processDelivery(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        orderRepository.save(order.get().setOrderId(orderId)
                .setOrderStatus(OrderStatus.PAID));

        List<Long> productIds = new ArrayList<>();
        order.get().getProductOrder().forEach(product -> productIds.add(product.getProductFk().getProductId()));

        Set<ProductOrder> productOrderList = order.get().getProductOrder();
        productOrderList.forEach(ProductOrder::decreaseProductAmount);
        productOrderRepository.saveAll(productOrderList);

        productRepository.incrementProductPopularity(productIds);
    }
}
