package com.example.demo.service;

import com.example.demo.model.Course;
import com.example.demo.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    // List courses with optional filtering and pagination
    public Page<Course> getCourses(String category, int difficultyLevel, Pageable pageable) {
        // If category is empty, pass null to repository method
        String cat = (category == null || category.isBlank()) ? null : category;
        return courseRepository.searchCourses(cat, difficultyLevel, pageable);
    }

    // Save a new course
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    // Get course by ID
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    // Delete course by ID
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}
