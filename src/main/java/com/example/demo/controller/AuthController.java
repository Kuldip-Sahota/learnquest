package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // ================= LOGIN =================

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("loggedInUser", userService.getLoggedInUser()); // navbar support
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, Model model) {

        System.out.println("Attempting login for username: " + user.getUsername());
        userService.userRepository.findByUsername(user.getUsername())
                .ifPresent(u -> System.out.println("DB password hash: " + u.getPassword()));

        boolean authenticated = userService.authenticate(
                user.getUsername(),
                user.getPassword()
        );

        System.out.println("Authentication result: " + authenticated);

        if (authenticated) {
            User loggedInUser = userService.getLoggedInUser();
            System.out.println("Logged in user role: " + loggedInUser.getRole());

            switch (loggedInUser.getRole().toLowerCase()) {
                case "admin":
                    return "redirect:/admin";
                case "teacher":
                case "student":
                    return "redirect:/courses";
                default:
                    userService.logout();
                    model.addAttribute("error", "Invalid role");
                    model.addAttribute("loggedInUser", null);
                    return "login";
            }

        } else {
            model.addAttribute("error", "Invalid username or password");
            model.addAttribute("loggedInUser", null);
            return "login";
        }
    }

    // ================= REGISTER =================

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("loggedInUser", userService.getLoggedInUser()); // navbar
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String role,
                           Model model) {

        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setRole(role);

            userService.saveUser(user); // 🔥 bcrypt happens here

            System.out.println("Registered new user: " + username + " with role " + role);

            return "redirect:/login";

        } catch (Exception e) {
            System.out.println("Registration failed for username: " + username + " - " + e.getMessage());
            model.addAttribute("error", "Username already exists");
            model.addAttribute("loggedInUser", null);
            return "register";
        }
    }

    // ================= LOGOUT =================

    @GetMapping("/logout")
    public String logout() {
        userService.logout();
        return "redirect:/login?logout";
    }
}