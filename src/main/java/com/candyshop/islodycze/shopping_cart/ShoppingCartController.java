package com.candyshop.islodycze.shopping_cart;

import com.candyshop.islodycze.model.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ShoppingCartController {

    @Autowired
    CartItemRepository repository;

    @GetMapping("/shopping_cart")
    public String showRegistrationForm(Model model) {
//        List<CartItem> cartItems = repository.findAllByUserUserId(1l);//TODO
        List<CartItem> cartItems = repository.findAll();//TODO
        model.addAttribute("cartItems", cartItems);

        return "shopping_cart";
    }
}
