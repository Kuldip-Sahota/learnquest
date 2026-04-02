package com.example.demo.controller;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("loggedInUser", userService.getLoggedInUser());
        return "home"; 
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("loggedInUser", userService.getLoggedInUser());
        return "about"; 
    }
}