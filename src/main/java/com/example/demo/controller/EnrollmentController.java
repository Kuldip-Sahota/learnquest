package com.example.demo.controller;

import com.example.demo.model.Enrollment;
import com.example.demo.model.User;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/enroll")
public class EnrollmentController {

    @Autowired
    private EnrollmentRepository repo;

    @Autowired
    private UserService userService;

    // CLICK BUTTON WILL HIT THIS
    @GetMapping("/{courseId}")
    public String enroll(@PathVariable Long courseId) {

        User user = userService.getLoggedInUser();

        if (user == null) {
            return "redirect:/login";
        }

        // prevent teacher/admin enrolling if you want (optional)
        if (!user.getRole().equalsIgnoreCase("STUDENT")) {
            return "redirect:/courses?error=notstudent";
        }

        Enrollment e = new Enrollment();
        e.setCourseId(courseId);
        e.setStudentId(user.getId());

        repo.save(e);

        return "redirect:/courses?enrolled=true";
    }
}