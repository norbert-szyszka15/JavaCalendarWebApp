package org.example.javacalendarwebapp.priority;

import org.springframework.stereotype.Component;

@Component
public class HighPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "High Priority";
    }
}