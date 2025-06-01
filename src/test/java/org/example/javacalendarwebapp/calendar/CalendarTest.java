package org.example.javacalendarwebapp.calendar;

import org.example.javacalendarwebapp.event.Event;
import org.example.javacalendarwebapp.task.Task;
import org.example.javacalendarwebapp.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CalendarTest {

    private Calendar calendar;

    @BeforeEach
    void setUp() {
        calendar = new Calendar();
    }

    @Test
    void whenNewCalendar_thenCollectionsInitializedAndIdIsNull() {
        assertNull(calendar.getId(), "Expected id to be null on new Calendar");
        assertNotNull(calendar.getUsers(), "Users set should be initialized");
        assertTrue(calendar.getUsers().isEmpty(), "Users set should start empty");
        assertNotNull(calendar.getTasks(), "Tasks set should be initialized");
        assertTrue(calendar.getTasks().isEmpty(), "Tasks set should start empty");
        assertNotNull(calendar.getEvents(), "Events set should be initialized");
        assertTrue(calendar.getEvents().isEmpty(), "Events set should start empty");
    }

    @Test
    void whenSetName_thenNameReturnedByGetter() {
        String testName = "My Test Calendar";
        calendar.setName(testName);
        assertEquals(testName, calendar.getName(), "Getter should return the name that was set");
    }

    @Test
    void whenAddUser_thenUserAppearsInUsersSet() {
        User user = new User();
        user.setUsername("alice");

        calendar.getUsers().add(user);
        Set<User> users = calendar.getUsers();

        assertEquals(1, users.size(), "Users set should contain exactly one element");
        assertTrue(users.contains(user), "Users set should contain the added user");
    }

    @Test
    void whenRemoveUser_thenUsersSetReflectsRemoval() {
        User user = new User();
        user.setUsername("bob");

        calendar.getUsers().add(user);
        assertTrue(calendar.getUsers().contains(user), "Users set should contain the user after addition");

        calendar.getUsers().remove(user);
        assertFalse(calendar.getUsers().contains(user), "Users set should not contain the user after removal");
        assertTrue(calendar.getUsers().isEmpty(), "Users set should be empty after removal");
    }

    @Test
    void whenAddTask_thenTaskAppearsInTasksSet() {
        Task task = new Task();
        task.setTitle("Test Task");

        calendar.getTasks().add(task);
        Set<Task> tasks = calendar.getTasks();

        assertEquals(1, tasks.size(), "Tasks set should contain exactly one element");
        assertTrue(tasks.contains(task), "Tasks set should contain the added task");
    }

    @Test
    void whenRemoveTask_thenTasksSetReflectsRemoval() {
        Task task = new Task();
        task.setTitle("Another Task");

        calendar.getTasks().add(task);
        assertTrue(calendar.getTasks().contains(task), "Tasks set should contain the task after addition");

        calendar.getTasks().remove(task);
        assertFalse(calendar.getTasks().contains(task), "Tasks set should not contain the task after removal");
        assertTrue(calendar.getTasks().isEmpty(), "Tasks set should be empty after removal");
    }

    @Test
    void whenAddEvent_thenEventAppearsInEventsSet() {
        Event event = new Event();
        event.setTitle("Test Event");

        calendar.getEvents().add(event);
        Set<Event> events = calendar.getEvents();

        assertEquals(1, events.size(), "Events set should contain exactly one element");
        assertTrue(events.contains(event), "Events set should contain the added event");
    }

    @Test
    void whenRemoveEvent_thenEventsSetReflectsRemoval() {
        Event event = new Event();
        event.setTitle("Another Event");

        calendar.getEvents().add(event);
        assertTrue(calendar.getEvents().contains(event), "Events set should contain the event after addition");

        calendar.getEvents().remove(event);
        assertFalse(calendar.getEvents().contains(event), "Events set should not contain the event after removal");
        assertTrue(calendar.getEvents().isEmpty(), "Events set should be empty after removal");
    }
}
