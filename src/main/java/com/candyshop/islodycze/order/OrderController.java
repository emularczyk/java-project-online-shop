package com.candyshop.islodycze.order;

import com.candyshop.islodycze.exceptions.ApplicationException;
import com.candyshop.islodycze.model.Order;
import com.candyshop.islodycze.registration.UserRepository;
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

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserRepository userRepository;

    //List of all orders for admin.
    @GetMapping("/order_list")
    public String orderList(final Model model) {
        List<Order> orderList = orderRepository.findAll();

        cutMilisecondsInDate(orderList);

        model.addAttribute("orderList", orderList);

        return "order_list";
    }

    //List of all orders for user.
    @GetMapping("/user_order_list")
    public String userOrderList(final Model model, final Principal principalUser) {
        Long userId = getUserId((UsernamePasswordAuthenticationToken) principalUser);
        List<Order> orderList = orderRepository.findAllByUserIdFkUserId(userId);

        cutMilisecondsInDate(orderList);

        model.addAttribute("orderList", orderList);

        return "order_list";
    }

    //First step of order - user approves a shopping cart state.
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

    //Third step of order - user approves a payment information.
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

    private void cutMilisecondsInDate(List<Order> orderList) {
        for (Order order : orderList) {
            LocalDateTime orderDate = order.getOrderDate();
            orderDate = orderDate.truncatedTo(ChronoUnit.SECONDS);
            order.setOrderDate(orderDate);
        }
    }

    private Long getUserId(UsernamePasswordAuthenticationToken principalUser) {
        Object principal = principalUser.getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        return userRepository.findByEmail(username)
                .getUserId();
    }
}
