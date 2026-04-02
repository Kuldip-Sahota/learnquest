package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by username (needed for login)
    Optional<User> findByUsername(String username);
    
    // Check if a username already exists (optional, for registration)
    boolean existsByUsername(String username);
}