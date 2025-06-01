package org.example.javacalendarwebapp.task.priority;

import org.springframework.stereotype.Component;

@Component
public class MediumPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "Medium Priority";
    }
}