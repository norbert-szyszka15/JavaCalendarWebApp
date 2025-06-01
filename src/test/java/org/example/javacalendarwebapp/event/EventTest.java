package org.example.javacalendarwebapp.event;

import org.example.javacalendarwebapp.calendar.Calendar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    private Event event;

    @BeforeEach
    void setUp() {
        event = new Event();
    }

    @Test
    void whenNewEvent_thenIdIsNullAndFieldsAreDefault() {
        assertNull(event.getId(), "Expected id to be null on new Event");
        assertNull(event.getTitle(), "Expected title to be null on new Event");
        assertNull(event.getDescription(), "Expected description to be null on new Event");
        assertNull(event.getDate(), "Expected date to be null on new Event");
        assertNull(event.getCalendar(), "Expected calendar to be null on new Event");
    }

    @Test
    void whenSetAndGetId_thenReturnCorrectValue() {
        event.setId(42L);
        assertEquals(42L, event.getId(), "Getter should return the id that was set");
    }

    @Test
    void whenSetAndGetTitle_thenReturnCorrectValue() {
        String title = "Team Meeting";
        event.setTitle(title);
        assertEquals(title, event.getTitle(), "Getter should return the title that was set");
    }

    @Test
    void whenSetAndGetDescription_thenReturnCorrectValue() {
        String desc = "Monthly project sync";
        event.setDescription(desc);
        assertEquals(desc, event.getDescription(), "Getter should return the description that was set");
    }

    @Test
    void whenSetAndGetDate_thenReturnCorrectValue() {
        LocalDateTime now = LocalDateTime.now();
        event.setDate(now);
        assertEquals(now, event.getDate(), "Getter should return the date that was set");
    }

    @Test
    void whenSetAndGetCalendar_thenReturnCorrectValue() {
        Calendar cal = new Calendar();
        cal.setId(100L);
        cal.setName("Work Calendar");

        event.setCalendar(cal);
        assertNotNull(event.getCalendar(), "Calendar should not be null after setting");
        assertEquals(100L, event.getCalendar().getId(), "Getter should return the calendar with the correct id");
        assertEquals("Work Calendar", event.getCalendar().getName(), "Getter should return the calendar with the correct name");
    }
}
