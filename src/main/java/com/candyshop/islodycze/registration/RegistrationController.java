package com.candyshop.islodycze.registration;

import com.candyshop.islodycze.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class RegistrationController {

        @Autowired
        private UserRepository userRepo;

        @Autowired
        private UserServices service;

        @GetMapping("")
        public String viewHomePage() {
            return "main_page";
        }

        @GetMapping("/register")
        public String showRegistrationForm(Model model) {
                model.addAttribute("user", new User());

                return "signup_form";
        }

        @GetMapping("/users")
        public String listUsers(Model model) {
                List<User> listUsers = userRepo.findAll();
                model.addAttribute("listUsers", listUsers);

                return "users";
        }

        @PostMapping("/process_register")
        public String processRegister(User user, HttpServletRequest request)
                throws UnsupportedEncodingException, MessagingException {
                service.register(user, getSiteURL(request));
                return "register_success";
        }

        private String getSiteURL(HttpServletRequest request) {
                String siteURL = request.getRequestURL().toString();
                return siteURL.replace(request.getServletPath(), "");
        }

        @GetMapping("/verify")
        public String verifyUser(@Param("code") String code) {
                if (service.verify(code)) {
                        return "verify_success";
                } else {
                        return "verify_fail";
                }
        }
}
