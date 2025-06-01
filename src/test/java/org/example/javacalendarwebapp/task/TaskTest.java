package org.example.javacalendarwebapp.task;

import org.example.javacalendarwebapp.calendar.Calendar;
import org.example.javacalendarwebapp.task.priority.TaskPriorityType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
    }

    @Test
    void whenNewTask_thenFieldsAreDefault() {
        assertNull(task.getId(), "Id powinno być null w nowym Task");
        assertNull(task.getTitle(), "Title powinno być null w nowym Task");
        assertNull(task.getDescription(), "Description powinno być null w nowym Task");
        assertNull(task.getPriority(), "Priority powinno być null w nowym Task");
        assertNull(task.getDate(), "Date powinno być null w nowym Task");
        assertFalse(task.getCompleted(), "Completed powinno domyślnie być false");
        assertNull(task.getCalendar(), "Calendar powinno być null w nowym Task");
    }

    @Test
    void whenSetAndGetId_thenReturnCorrectValue() {
        task.setId(99L);
        assertEquals(99L, task.getId(), "Getter powinien zwrócić id, które zostało ustawione");
    }

    @Test
    void whenSetAndGetTitle_thenReturnCorrectValue() {
        String title = "Buy groceries";
        task.setTitle(title);
        assertEquals(title, task.getTitle(), "Getter powinien zwrócić title, które zostało ustawione");
    }

    @Test
    void whenSetAndGetDescription_thenReturnCorrectValue() {
        String desc = "Milk, Bread, Eggs";
        task.setDescription(desc);
        assertEquals(desc, task.getDescription(), "Getter powinien zwrócić description, które zostało ustawione");
    }

    @Test
    void whenSetAndGetPriority_thenReturnCorrectValue() {
        task.setPriority(TaskPriorityType.HIGH);
        assertEquals(TaskPriorityType.HIGH, task.getPriority(), "Getter powinien zwrócić priority, które zostało ustawione");
    }

    @Test
    void whenSetAndGetDate_thenReturnCorrectValue() {
        LocalDateTime now = LocalDateTime.of(2025, 6, 2, 15, 45);
        task.setDate(now);
        assertEquals(now, task.getDate(), "Getter powinien zwrócić date, które zostało ustawione");
    }

    @Test
    void whenSetAndGetCompleted_thenReturnCorrectValue() {
        assertFalse(task.getCompleted(), "Domyślnie completed powinno być false");
        task.setCompleted(true);
        assertTrue(task.getCompleted(), "Getter powinien zwrócić true, po ustawieniu completed na true");
    }

    @Test
    void whenSetAndGetCalendar_thenReturnCorrectValue() {
        Calendar cal = new Calendar();
        cal.setId(42L);
        cal.setName("Work Calendar");

        task.setCalendar(cal);
        assertNotNull(task.getCalendar(), "Calendar nie powinno być null po ustawieniu");
        assertEquals(42L, task.getCalendar().getId(), "Getter powinien zwrócić calendar z poprawnym id");
        assertEquals("Work Calendar", task.getCalendar().getName(), "Getter powinien zwrócić calendar z poprawną nazwą");
    }
}
