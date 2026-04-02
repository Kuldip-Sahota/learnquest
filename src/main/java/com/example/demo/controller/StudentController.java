package com.example.demo.controller;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentController {

    @Autowired
    private UserService userService;

    @GetMapping("/student")
    public String studentPage(Model model) {
        // Add logged-in user for navbar/role checks
        model.addAttribute("loggedInUser", userService.getLoggedInUser());
        return "student"; // loads student.html
    }
}