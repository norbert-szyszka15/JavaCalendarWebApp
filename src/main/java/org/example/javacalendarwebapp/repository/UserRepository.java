package org.example.javacalendarwebapp.repository;

import org.example.javacalendarwebapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}