package org.example.javacalendarwebapp.priority;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PriorityLevelTest {

    @Test
    void highPriorityReturnsCorrectValue() {
        PriorityLevel high = new HighPriority();
        assertEquals("High Priority", high.getPriority());
    }

    @Test
    void mediumPriorityReturnsCorrectValue() {
        PriorityLevel medium = new MediumPriority();
        assertEquals("Medium Priority", medium.getPriority());
    }

    @Test
    void lowPriorityReturnsCorrectValue() {
        PriorityLevel low = new LowPriority();
        assertEquals("Low Priority", low.getPriority());
    }
}
