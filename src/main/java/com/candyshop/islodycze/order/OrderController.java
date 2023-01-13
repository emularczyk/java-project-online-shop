package com.candyshop.islodycze.order;

import com.candyshop.islodycze.exceptions.ApplicationException;
import com.candyshop.islodycze.exceptions.NoItemsInCartException;
import com.candyshop.islodycze.exceptions.NotEnoughProducts;
import com.candyshop.islodycze.loyalty_points.LoyaltyPointsService;
import com.candyshop.islodycze.model.*;
import com.candyshop.islodycze.model.enums.OrderStatus;
import com.candyshop.islodycze.registration.UserRepository;
import com.candyshop.islodycze.shopping_cart.CartItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;

    @GetMapping("/order_list")
    public String orderList(final Model model) {
        List<Order> orderList = orderRepository.findAll();

        model.addAttribute("orderList", orderList);

        return "order_list";
    }

    @PostMapping("/process_order")
    public String processOrder(final String cartPrice, final Principal principalUser, final boolean isDiscount,
                               final Model model) {
        Long orderId;
        try {
            orderId = orderService.processOrder(cartPrice, (UsernamePasswordAuthenticationToken) principalUser,
                    isDiscount);
        } catch (ApplicationException e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }

        return "redirect:/delivery_form/" + orderId;
    }

    @Transactional
    @PostMapping("/process_payment/{orderId}")
    public String processDelivery(@PathVariable("orderId") final Long orderId, final Model model) {

        try {
            orderService.processDelivery(orderId);
        } catch (ApplicationException e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }

        return "order_success";
    }
}
