package com.candyshop.islodycze.order;

import com.candyshop.islodycze.exceptions.NoItemsInCartException;
import com.candyshop.islodycze.loyalty_points.LoyaltyPointsService;
import com.candyshop.islodycze.mainPage.ProductRepository;
import com.candyshop.islodycze.model.CartItem;
import com.candyshop.islodycze.model.Order;
import com.candyshop.islodycze.model.ProductOrder;
import com.candyshop.islodycze.model.UserEntity;
import com.candyshop.islodycze.model.enums.OrderStatus;
import com.candyshop.islodycze.registration.UserRepository;
import com.candyshop.islodycze.shopping_cart.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductOrderRepository productOrderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    void processDelivery(Long orderId) {
        Order order = orderRepository.findById(orderId)
                                     .get();
        orderRepository.save(order.setOrderId(orderId)
                .setOrderStatus(OrderStatus.PAID));

        List<Long> productIds = new ArrayList<>();
        order.getProductOrder()
             .forEach(product -> productIds.add(product.getProductFk()
                                                       .getProductId()));

        if (productIds.isEmpty()) {
            throw new NoItemsInCartException("There is not items in your shopping cart.");
        }

        Set<ProductOrder> productOrderList = order.getProductOrder();
        productOrderList.forEach(ProductOrder::decreaseProductAmount);
        productOrderRepository.saveAll(productOrderList);

        productRepository.incrementProductPopularity(productIds);
        order.addLoyaltyPoints();
        if (order.isDiscount()) {
            order.removeLoyaltyPoints(10);
        }
    }

    Long processOrder(String cartPrice, UsernamePasswordAuthenticationToken principalUser, boolean isDiscount) {
        Long userId = getUserId(principalUser);

        Set<CartItem> cartItems = cartItemRepository.findAllByUserUserId(userId);
        if (cartItems.isEmpty()) {
            throw new NoItemsInCartException("There is not items in your shopping cart.");
        }
        Set<ProductOrder> productToOrderSet = createProductToOrderSet(cartItems);
        return orderRepository.save(Order.builder()
                .productOrder(productToOrderSet)
                .totalCost(LoyaltyPointsService.resolveDiscount(priceWithCurrencyToDouble(cartPrice),
                        isDiscount))
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.CREATED)
                .isDiscount(isDiscount)
                .userIdFk(new UserEntity().setUserId(userId))
                .build()).getOrderId();
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
