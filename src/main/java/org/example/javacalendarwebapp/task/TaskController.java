package org.example.javacalendarwebapp.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Task Management", description = "Manage tasks within the calendar application")
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "Get all tasks", description = "Retrieve a list of all tasks associated with the calendar.")
    public List<Task> getAllTasks() {
        return taskService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by id", description = "Retrieve a specific task based on its id.")
    public Optional<Task> getTaskById(
        @Parameter(description = "ID of the task to retrieve", required = true)
        @PathVariable Long id
    ) {
        return Optional.ofNullable(taskService.findById(id).orElseThrow(() -> new RuntimeException("Task not found with id: " + id)));
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new task", description = "Create a new task with the provided details.")
    public ResponseEntity<Task> createTask(
        @Parameter(description = "Details of the task to create", required = true)
        @Param("task") Task task
    ) {
        Task createdTask = taskService.create(task);
        return ResponseEntity.ok(createdTask);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing task", description = "Update the details of an existing task.")
    public ResponseEntity<Task> updateTask(
        @Parameter(description = "ID of the task to update", required = true)
        @PathVariable Long id,
        @Parameter(description = "Updated details of the task", required = true)
        @Param("task") Task task
    ) {
        Task updatedTask = taskService.update(id, task);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task", description = "Delete a specific task based on its id.")
    public ResponseEntity<Void> deleteTask(
        @Parameter(description = "ID of the task to delete", required = true)
        @PathVariable Long id
    ) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/date")
    @Operation(summary = "Get task date", description = "Returns only the task's date for given ID")
    public ResponseEntity<LocalDateTime> getTaskDateById(
            @Parameter(description = "ID of the task", required = true)
            @PathVariable Long id
    ) {
        try {
            LocalDateTime date = taskService.getTaskDateById(id);
            if (date == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(date);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/completed")
    @Operation(summary = "Get all completed tasks", description = "Retrieve a list of all completed tasks associated with the calendar.")
    public ResponseEntity<List<Task>> getAllCompletedTasks() {
        List<Task> completed = taskService.findAllCompletedTasks();
        return ResponseEntity.ok(completed);
    }

    @GetMapping("/uncompleted")
    @Operation(summary = "Get all uncompleted tasks", description = "Retrieve a list of all uncompleted tasks associated with the calendar.")
    public ResponseEntity<List<Task>> getAllUncompletedTasks() {
        List<Task> uncompleted = taskService.findAllUncompletedTasks();
        return ResponseEntity.ok(uncompleted);
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "Mark a task as completed", description = "Mark a specific task as completed based on its id.")
    public ResponseEntity<Task> markTaskAsCompleted(
            @Parameter(description = "ID of the task to mark as completed", required = true)
            @PathVariable Long id
    ) {
        try {
            Task updated = taskService.markAsCompleted(id);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}