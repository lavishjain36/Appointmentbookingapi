package com.example.appointment.repository;

import com.example.appointment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Database access for users referenced by security principal email.
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
