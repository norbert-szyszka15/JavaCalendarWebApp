package org.example.javacalendarwebapp.service;

import org.example.javacalendarwebapp.entity.Task;
import org.example.javacalendarwebapp.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    public Task create(Task task) {
        return taskRepository.save(task);
    }

    public Task update(Long id, Task task) {
        if (!taskRepository.existsById(id)) {
            return null; // or throw an exception
        }
        task.setId(id);
        return taskRepository.save(task);
    }

    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            return; // or throw an exception
        }
        taskRepository.deleteById(id);
    }

    public LocalDateTime getTaskDateById(Long id) {
        Task t = taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + id));
        return t.getDate();
    }
}
