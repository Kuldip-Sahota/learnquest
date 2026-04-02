package com.example.demo.controller;

import com.example.demo.model.Course;
import com.example.demo.model.User;
import com.example.demo.repository.CourseRepository;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserService userService;

    // ================= VIEW COURSES =================

    @GetMapping
    public String listCourses(
            @RequestParam(defaultValue = "") String category,
            @RequestParam(defaultValue = "0") int difficultyLevel,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "name") String sortBy,
            Model model
    ) {
        // Validate sortBy
        if (!sortBy.equals("name") && !sortBy.equals("category") && !sortBy.equals("difficultyLevel")) {
            sortBy = "name";
        }

        Page<Course> courses = courseRepository.searchCourses(
                category.isBlank() ? null : category,
                difficultyLevel,
                PageRequest.of(page, 5, Sort.by(sortBy))
        );

        model.addAttribute("courses", courses.getContent());
        model.addAttribute("category", category);
        model.addAttribute("difficultyLevel", difficultyLevel);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", courses.getTotalPages());

        model.addAttribute("loggedInUser", userService.getLoggedInUser());

        return "courses";
    }

    // ================= ADD COURSE (FORM) =================

    @GetMapping("/add")
    public String showForm(Model model) {

        User user = userService.getLoggedInUser();

        //  Not logged in → go to login
        if (user == null) {
            return "redirect:/login";
        }

        // Logged in but not teacher/admin
        if (!userService.isTeacherOrAdmin()) {
            return "redirect:/courses?error=notauthorized";
        }

        model.addAttribute("course", new Course());
        model.addAttribute("loggedInUser", user);

        return "course_form";
    }

    // ================= ADD COURSE (SUBMIT) =================

    @PostMapping("/add")
    public String addCourse(@Valid @ModelAttribute("course") Course course,
                            BindingResult result,
                            Model model) {

        User user = userService.getLoggedInUser();

        //  Not logged in
        if (user == null) {
            return "redirect:/login";
        }

        //  Not authorized
        if (!userService.isTeacherOrAdmin()) {
            return "redirect:/courses?error=notauthorized";
        }

        // Validation errors
        if (result.hasErrors()) {
            model.addAttribute("loggedInUser", user);
            return "course_form";
        }

        courseRepository.save(course);
        return "redirect:/courses";
    }
}