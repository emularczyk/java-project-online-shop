package com.candyshop.islodycze.registration;

import com.candyshop.islodycze.model.Enum.Role;
import com.candyshop.islodycze.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class RegistrationController {

        @Autowired
        private UserRepository userRepo;

        @GetMapping("")
        public String viewHomePage() {
            return "index";
        }

        @GetMapping("/register")
        public String showRegistrationForm(Model model) {
                model.addAttribute("user", new User());

                return "signup_form";
        }

        @PostMapping("/process_register")
        public String processRegister(User user) {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodedPassword = passwordEncoder.encode(user.getUserPassword());
                user.setUserPassword(encodedPassword);
                user.setRole(Role.USER);

                userRepo.save(user);

                return "register_success";
        }

        @GetMapping("/users")
        public String listUsers(Model model) {
                List<User> listUsers = userRepo.findAll();
                model.addAttribute("listUsers", listUsers);

                return "users";
        }
}
