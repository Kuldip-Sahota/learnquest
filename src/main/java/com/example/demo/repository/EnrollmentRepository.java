package com.example.demo.repository;

import com.example.demo.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
}