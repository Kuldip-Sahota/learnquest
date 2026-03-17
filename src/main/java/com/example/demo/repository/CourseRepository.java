package com.example.demo.repository;

import com.example.demo.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("""
        SELECT c FROM Course c
        WHERE (:category IS NULL OR LOWER(c.category) LIKE LOWER(CONCAT('%', :category, '%')))
        AND (:difficultyLevel = 0 OR c.difficultyLevel = :difficultyLevel)
    """)
    Page<Course> searchCourses(
        @Param("category") String category,
        @Param("difficultyLevel") int difficultyLevel,
        Pageable pageable
    );
}