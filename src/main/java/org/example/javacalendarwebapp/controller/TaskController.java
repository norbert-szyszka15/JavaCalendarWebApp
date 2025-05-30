package org.example.javacalendarwebapp.controller;

import java.util.List;

import org.example.javacalendarwebapp.entity.Task;
import org.example.javacalendarwebapp.service.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Task Management", description = "Manage tasks within the calendar application")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "Get all tasks", description = "Retrieve a list of all tasks associated with the calendar.")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }
}