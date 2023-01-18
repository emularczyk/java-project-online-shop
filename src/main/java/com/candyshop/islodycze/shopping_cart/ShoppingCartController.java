package com.candyshop.islodycze.shopping_cart;

import com.candyshop.islodycze.loyalty_points.LoyaltyPointsService;
import com.candyshop.islodycze.model.*;
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

import java.math.RoundingMode;
import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
public class ShoppingCartController {

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private UserRepository userRepository;

    //List of user cart items.
    @GetMapping("/shopping_cart")
    public String shoppingCartList(final Model model, final Principal principalUser) {
        Object principal = ((UsernamePasswordAuthenticationToken) principalUser).getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        List<CartItem> cartItems = cartItemRepository.findAllByUserEmail(username);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartPrice", addCurrency(countCartPrice(cartItems)));
        model.addAttribute("cartPriceWithDiscount", addCurrency(getPriceWithDiscount(cartItems)));

        return "shopping_cart";
    }

    //User deletes item from its cart request.
    @Transactional
    @GetMapping("/shopping_cart/remove/{productId}")
    public String removeShoppingCartItem(@PathVariable final Long productId,
                                         final Principal principalUser) {

        Object principal = ((UsernamePasswordAuthenticationToken) principalUser).getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        cartItemRepository.deleteByUserEmailAndProductProductId(username, productId);

        return "redirect:/shopping_cart";
    }

    //User add item to its cart.
    @PostMapping("/shopping_cart/add_item")
    public String addItemsToShoppingCart(final Integer quantity, final Long productId, final Principal principalUser) {

        if(quantity == null) {
            log.error("Quantity is null");
            return "error";
        }

        if(productId == null) {
            log.error("productId is null");
            return "error";
        }

        if(principalUser == null) {
            log.info("User is null");
            return "redirect:/login";
        }

        Long userId = getUserId((UsernamePasswordAuthenticationToken) principalUser);

        CartItem duplicatedItem =
                cartItemRepository.findByUserUserIdAndProductProductId(userId, productId);

        //Prevent duplicating of items in cart
        if (duplicatedItem == null) {
            cartItemRepository.save(CartItem.builder()
                                            .product(new Product().setProductId(productId))
                                            .user(new UserEntity().setUserId(userId))
                                            .quantity(quantity)
                                            .build());
        } else {
            cartItemRepository.save(CartItem.builder()
                                            .id(duplicatedItem.getId())
                                            .product(new Product().setProductId(productId))
                                            .user(new UserEntity().setUserId(userId))
                                            .quantity(duplicatedItem.getQuantity() + quantity)
                                            .build());
        }

        return "redirect:/product/" + productId;
    }

    private Double getPriceWithDiscount(List<CartItem> cartItems) {
        return LoyaltyPointsService.resolveDiscount(countCartPrice(cartItems))
                                   .setScale(2, RoundingMode.HALF_UP)
                                   .doubleValue();
    }

    private Double countCartPrice(final List<CartItem> cartItems) {
        return cartItems.stream()
                        .mapToDouble(item -> item.getProduct().getPrice()
                                                              .doubleValue() * item.getQuantity())
                        .sum();
    }

    private String addCurrency(final Double price) {
        return price.toString() + " zł";
    }

    private Long getUserId(UsernamePasswordAuthenticationToken principalUser) {
        Object principal = principalUser.getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        return userRepository.findByEmail(username)
                             .getUserId();
    }
}
