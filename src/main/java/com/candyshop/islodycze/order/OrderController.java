package com.candyshop.islodycze.order;

import com.candyshop.islodycze.model.CartItem;
import com.candyshop.islodycze.model.enums.OrderStatus;
import com.candyshop.islodycze.model.Order;
import com.candyshop.islodycze.model.ProductOrder;
import com.candyshop.islodycze.model.UserEntity;
import com.candyshop.islodycze.registration.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class OrderController {

    @Autowired
    private OrderRepository repository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/order_list")
    public String orderList(final Model model) {
        List<Order> orderList = repository.findAll();

        model.addAttribute("orderList", orderList);

        return "order_list";
    }

    @PostMapping("/process_order")
    public String processOrder(final String cartPrice, final HashSet<CartItem> cartItems,
                               final Principal principalUser) {
        Long userId = getUserId((UsernamePasswordAuthenticationToken) principalUser);

        Long orderId = repository.save(Order.builder()
                             .productOrderFk(createProductToOrderSet(cartItems))
                             .totalCost(BigDecimal.valueOf(priceWithCurrencyToDouble(cartPrice)))
                             .orderDate(LocalDateTime.now())
                             .orderStatus(OrderStatus.CREATED)
                             .userIdFk(new UserEntity().setUserId(userId))
                             .build()).getOrderId();
        return "redirect:/delivery_form/" + orderId;
    }

    private Long getUserId(UsernamePasswordAuthenticationToken principalUser) {
        Object principal = principalUser.getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        return userRepository.findByEmail(username)
                             .getUserId();
    }

    private Set<ProductOrder> createProductToOrderSet (final Set<CartItem> cartItems) {
        Set<ProductOrder> products = new HashSet<>();
        cartItems.forEach(item -> products.add(new ProductOrder().builder()
                                                                 .productFk(item.getProduct())
                                                                 .amount(item.getQuantity())
                                                                 .build()));
        return products;
    }

    private Double priceWithCurrencyToDouble(final String price) {
        return Double.parseDouble(removeLastChars(price, 3));
    }

    private String removeLastChars(final String str, final int chars) {
        return str.substring(0, str.length() - chars);
    }
}
