package org.example.javacalendarwebapp.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskPriorityEnumTest {

    @Test
    void enumValues() {
        assertEquals(3, TaskPriority.values().length);
        assertEquals(TaskPriority.LOW, TaskPriority.valueOf("LOW"));
        assertEquals(TaskPriority.MEDIUM, TaskPriority.valueOf("MEDIUM"));
        assertEquals(TaskPriority.HIGH, TaskPriority.valueOf("HIGH"));
    }
}
