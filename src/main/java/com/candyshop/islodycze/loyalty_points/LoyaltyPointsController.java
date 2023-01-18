package com.candyshop.islodycze.loyalty_points;

import com.candyshop.islodycze.model.UserEntity;
import com.candyshop.islodycze.registration.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoyaltyPointsController {

    @Autowired
    private UserRepository userRepo;

    //Rest endpoint to tighten the data for navigation bar.
    @GetMapping("/products/{username}")
    public UserEntity getUserLoyaltyPoints(@PathVariable("username") String username) {
        UserEntity userEntity = userRepo.findByEmail(username);
        return new UserEntity().setLoyaltyPoints(userEntity.getLoyaltyPoints());
    }

}
