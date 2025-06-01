package org.example.javacalendarwebapp.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskEntityTest {

    @Test
    void testTaskGettersAndSetters() {
        Task task = new Task();
        task.setId(20L);
        task.setTitle("Title");
        task.setDescription("Desc");
        task.setPriority(TaskPriority.HIGH);
        LocalDateTime dt = LocalDateTime.of(2025, 7, 1, 10, 30);
        task.setDate(dt);
        task.setCompleted(true);

        Calendar cal = new Calendar();
        cal.setId(6L);
        cal.setName("Cal");
        task.setCalendar(cal);

        assertEquals(20L, task.getId());
        assertEquals("Title", task.getTitle());
        assertEquals("Desc", task.getDescription());
        assertEquals(TaskPriority.HIGH, task.getPriority());
        assertEquals(dt, task.getDate());
        assertTrue(task.getCompleted());
        assertSame(cal, task.getCalendar());
    }
}
