package org.example.javacalendarwebapp.task.priority;

import org.springframework.stereotype.Component;

@Component
public class HighPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "High Priority";
    }
}