package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // Admin Dashboard
    @GetMapping("/admin")
    public String adminPage(Model model) {
        User user = userService.getLoggedInUser();
        if (user == null) return "redirect:/login";
        if (!userService.hasRole("ADMIN")) return "redirect:/courses?error=notauthorized";

        model.addAttribute("loggedInUser", user);
        model.addAttribute("currentUser", user);
        List<User> allUsers = userRepository.findAll();
        model.addAttribute("users", allUsers);

        return "admin";
    }

    // Show add user form
    @GetMapping("/admin/add")
    public String showAddForm(Model model) {
        model.addAttribute("loggedInUser", userService.getLoggedInUser());
        model.addAttribute("user", new User());
        return "admin_user_form";
    }

    // Save new user (hash password)
    @PostMapping("/admin/add")
    public String addUser(@ModelAttribute User user) {
        user.setPassword(userService.encodePassword(user.getPassword()));
        userRepository.save(user);
        return "redirect:/admin";
    }

    // Show edit form
    @GetMapping("/admin/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElseThrow();
        model.addAttribute("loggedInUser", userService.getLoggedInUser());
        model.addAttribute("user", user);
        return "admin_user_form";
    }

    // Process edit (hash password if changed)
    @PostMapping("/admin/edit")
    public String editUser(@ModelAttribute User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(userService.encodePassword(user.getPassword()));
        } else {
            // Keep old password if field is empty
            String oldPassword = userRepository.findById(user.getId())
                    .map(User::getPassword)
                    .orElse("");
            user.setPassword(oldPassword);
        }
        userRepository.save(user);
        return "redirect:/admin";
    }

    // Delete user
    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin";
    }
}