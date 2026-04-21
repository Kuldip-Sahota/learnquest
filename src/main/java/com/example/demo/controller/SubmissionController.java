package com.example.demo.controller;

import com.example.demo.model.Submission;
import com.example.demo.service.UserService;
import com.example.demo.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class SubmissionController {

    @Autowired
    private SubmissionRepository repo;

    @Autowired
    private UserService userService;

    // ================= UPLOAD FILE =================
    @PostMapping("/submit/{courseId}")
    public String upload(
            @PathVariable Long courseId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        if (userService.getLoggedInUser() == null) {
            return "redirect:/login";
        }

        String uploadDir = "uploads/";
        new File(uploadDir).mkdirs();

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String filePath = uploadDir + fileName;

        file.transferTo(new File(filePath));

        Submission s = new Submission();
        s.setCourseId(courseId);
        s.setStudentId(userService.getLoggedInUser().getId());
        s.setFileName(file.getOriginalFilename());
        s.setFilePath(filePath);

        repo.save(s);

        return "redirect:/courses";
    }

    // ================= VIEW SUBMISSIONS (TEACHER) =================
    @GetMapping("/submission/{courseId}")
    public String view(@PathVariable Long courseId, Model model) {

        List<Submission> list = repo.findByCourseId(courseId);

        model.addAttribute("submissions", list);
        model.addAttribute("loggedInUser", userService.getLoggedInUser());

        return "submissions";
    }
}