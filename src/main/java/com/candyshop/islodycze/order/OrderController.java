package com.candyshop.islodycze.order;

import com.candyshop.islodycze.mainPage.ProductRepository;
import com.candyshop.islodycze.model.*;
import com.candyshop.islodycze.model.enums.OrderStatus;
import com.candyshop.islodycze.registration.UserRepository;
import com.candyshop.islodycze.shopping_cart.CartItemRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Log4j2
@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductOrderRepository productOrderRepository;

    @GetMapping("/order_list")
    public String orderList(final Model model) {
        List<Order> orderList = orderRepository.findAll();

        model.addAttribute("orderList", orderList);

        return "order_list";
    }

    @PostMapping("/process_order")
    public String processOrder(final String cartPrice,
                               final Principal principalUser) {
        Long userId = getUserId((UsernamePasswordAuthenticationToken) principalUser);

        Set<CartItem> cartItems = cartItemRepository.findAllByUserUserId(userId);
        Set<ProductOrder> productToOrderSet = createProductToOrderSet(cartItems);
        Long orderId = orderRepository.save(Order.builder()
                             .productOrder(productToOrderSet)
                             .totalCost(BigDecimal.valueOf(priceWithCurrencyToDouble(cartPrice)))
                             .orderDate(LocalDateTime.now())
                             .orderStatus(OrderStatus.CREATED)
                             .userIdFk(new UserEntity().setUserId(userId))
                             .build()).getOrderId();

        return "redirect:/delivery_form/" + orderId;
    }

    @PostMapping("/process_payment/{orderId}")
    public String processDelivery(@PathVariable("orderId") final Long orderId) {

        Optional<Order> order = orderRepository.findById(orderId);
        orderRepository.save(order.get().setOrderId(orderId)
                  .setOrderStatus(OrderStatus.PAID));

        List<Long> productIds = new ArrayList<>();
        order.get().getProductOrder().forEach(product -> productIds.add(product.getProductFk().getProductId()));
        List<Product> productList = productRepository.findAllById(productIds);
        productList.forEach(Product::incrementPopularity);
        productRepository.saveAll(productList);
        if (productList.isEmpty()) {
            log.info("No products to increment popularity.");
        }

        return "order_success";
    }

    private Long getUserId(UsernamePasswordAuthenticationToken principalUser) {
        Object principal = principalUser.getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        return userRepository.findByEmail(username)
                             .getUserId();
    }

    private Set<ProductOrder> createProductToOrderSet (final Set<CartItem> cartItems) {
        Set<ProductOrder> products = new HashSet<>();
        cartItems.forEach(item -> products.add(ProductOrder.builder()
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
