package org.example.javacalendarwebapp.priority;

import org.springframework.stereotype.Component;

@Component
public class LowPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "Low Priority";
    }
}