package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Category is required")
    private String category;

    @Min(value = 1, message = "Difficulty must be at least 1")
    @Max(value = 5, message = "Difficulty cannot exceed 5")
    @Column(name = "difficulty_level")
    private int difficultyLevel;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Course() { }

    public Course(String name, String category, int difficultyLevel) {
        this.name = name;
        this.category = category;
        this.difficultyLevel = difficultyLevel;
    }

    // Getters and setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(int difficultyLevel) { this.difficultyLevel = difficultyLevel; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}