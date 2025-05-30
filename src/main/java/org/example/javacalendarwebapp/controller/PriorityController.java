package org.example.javacalendarwebapp.controller;

import org.example.javacalendarwebapp.priority.PriorityLevel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Priority Management", description = "Manage priority levels for calendar events")
@RequestMapping("prorities")
public class PriorityController {
    private final List<PriorityLevel> priorityLevels;

    public PriorityController(List<PriorityLevel> priorityLevels) {
        this.priorityLevels = priorityLevels;
    }

    @GetMapping
    @Operation(summary = "Get all priority levels", description = "Retrieve a list of all available priority levels for calendar events.")
    public List<String> getPriorityLevels() {
        return priorityLevels.stream()
                .map(PriorityLevel::getPriority)
                .collect(Collectors.toList());
    }
}