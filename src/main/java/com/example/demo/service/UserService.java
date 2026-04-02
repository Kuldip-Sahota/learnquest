package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    private HttpSession session;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ====================== INIT DEFAULT USERS ======================
    @PostConstruct
    public void initUsers() {
        // Only create defaults if they don't already exist
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setRole("ADMIN");
            userRepository.save(admin);

            User teacher = new User();
            teacher.setUsername("teacher");
            teacher.setPassword(passwordEncoder.encode("password"));
            teacher.setRole("TEACHER");
            userRepository.save(teacher);

            User student = new User();
            student.setUsername("student");
            student.setPassword(passwordEncoder.encode("password"));
            student.setRole("STUDENT");
            userRepository.save(student);

            System.out.println("Default users created: admin, teacher, student (password: password)");
        }
    }

    // ====================== REGISTER OR SAVE USER ======================
    public User saveUser(User user) {
        // Hash password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Encode a raw password (helper for admin forms)
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    // ====================== AUTHENTICATE ======================
    public boolean authenticate(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            System.out.println("Authenticating user: " + username);
            System.out.println("Stored hash: " + user.getPassword());
            boolean matches = passwordEncoder.matches(password, user.getPassword());
            System.out.println("Password matches: " + matches);

            if (matches) {
                session.setAttribute("loggedInUser", user);
            }
            return matches;
        }
        System.out.println("User not found: " + username);
        return false;
    }

    // ====================== LOGOUT ======================
    public void logout() {
        session.invalidate();
    }

    // ====================== GET LOGGED-IN USER ======================
    public User getLoggedInUser() {
        return (User) session.getAttribute("loggedInUser");
    }

    // ====================== ROLE CHECKS ======================
    public boolean hasRole(String role) {
        User user = getLoggedInUser();
        return user != null && user.getRole().equalsIgnoreCase(role);
    }

    public boolean isTeacherOrAdmin() {
        User user = getLoggedInUser();
        if (user == null) return false;
        String role = user.getRole();
        return "TEACHER".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role);
    }
}