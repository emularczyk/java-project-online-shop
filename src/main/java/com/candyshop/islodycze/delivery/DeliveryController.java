package com.candyshop.islodycze.delivery;

import com.candyshop.islodycze.model.enums.OrderStatus;
import com.candyshop.islodycze.model.Order;
import com.candyshop.islodycze.order.OrderRepository;
import com.candyshop.islodycze.shopping_cart.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
public class DeliveryController {

    @Autowired
    private DeliveryRepository repository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @GetMapping("/delivery_form/{orderId}")
    public String deliveryForm(@PathVariable("orderId") final Long orderId, final Model model) {

        model.addAttribute("orderId", orderId);
        model.addAttribute("delivery", new DeliveryEntity());
        model.addAttribute("deliveryMode", DeliveryMode.values());
        model.addAttribute("countries", Country.values());

        return "delivery_form";
    }

        @GetMapping("/delivery_list")
    public String deliveryList(final Model model) {
        List<DeliveryEntity> deliveryList = repository.findAll();

        model.addAttribute("deliveryList", deliveryList);

        return "delivery_list";
    }

    @Transactional
    @PostMapping("/process_delivery/{orderId}")
    public String processDelivery(@PathVariable("orderId") final Long orderId, final DeliveryEntity delivery) {
        delivery.setOrder(new Order().setOrderId(orderId))
                .setDeliveryCost(BigDecimal.valueOf(delivery.getMode()
                                                            .price));
        repository.save(delivery);
        Optional<Order> order = orderRepository.findById(orderId);
        orderRepository.save(order.get().setOrderId(orderId)
                                        .setOrderStatus(OrderStatus.COMPLETED));
        cartItemRepository.deleteAllByUserUserId(order.get()
                                                      .getUserIdFk()
                                                      .getUserId());
        return "payment";
    }
}
