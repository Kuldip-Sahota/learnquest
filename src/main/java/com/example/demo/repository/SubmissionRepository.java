package com.example.demo.repository;

import com.example.demo.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByCourseId(Long courseId);
}