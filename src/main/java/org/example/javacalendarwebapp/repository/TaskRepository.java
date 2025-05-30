package org.example.javacalendarwebapp.repository;

import org.example.javacalendarwebapp.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> { 
}