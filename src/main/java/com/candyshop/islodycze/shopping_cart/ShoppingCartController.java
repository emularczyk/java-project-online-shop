package com.candyshop.islodycze.shopping_cart;

import com.candyshop.islodycze.model.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
public class ShoppingCartController {

    @Autowired
    private CartItemRepository repository;

    @GetMapping("/shopping_cart")
    public String shoppingCartList(final Model model, final Principal principalUser) {
        Object principal = ((UsernamePasswordAuthenticationToken) principalUser).getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        List<CartItem> cartItems = repository.findAllByUserEmail(username);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartPrice", addCurrency(countCartPrice(cartItems)));

        return "shopping_cart";
    }

    @Transactional
    @GetMapping("/shopping_cart/remove/{productId}")
    public String removeShoppingCartItem(@PathVariable final Long productId,
                                         final Principal principalUser) {

        Object principal = ((UsernamePasswordAuthenticationToken) principalUser).getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        repository.deleteByUserEmailAndProductProductId(username, productId);

        return "redirect:/shopping_cart";
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
}
