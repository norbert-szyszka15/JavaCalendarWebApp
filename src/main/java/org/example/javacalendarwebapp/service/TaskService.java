package org.example.javacalendarwebapp.service;

import org.example.javacalendarwebapp.entity.Task;
import org.example.javacalendarwebapp.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return Optional.of(taskRepository.findById(id).orElse(null));
    }
}
