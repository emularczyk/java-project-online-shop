package com.candyshop.islodycze.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegistrationController {

        @Autowired
        private UserRepository userRepo;

        @GetMapping("")
        public String viewHomePage() {
            return "index";
        }
}
