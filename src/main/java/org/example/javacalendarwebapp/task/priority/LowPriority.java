package org.example.javacalendarwebapp.task.priority;

import org.springframework.stereotype.Component;

@Component
public class LowPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "Low Priority";
    }
}