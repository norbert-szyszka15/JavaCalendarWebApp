package org.example.javacalendarwebapp.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCompletedTrue();
    List<Task> findByCompletedFalse();
}