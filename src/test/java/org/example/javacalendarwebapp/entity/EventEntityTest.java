package org.example.javacalendarwebapp.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EventEntityTest {

    @Test
    void testEventGettersAndSetters() {
        Event event = new Event();
        event.setId(30L);
        event.setTitle("Meeting");
        event.setDescription("DescEv");
        LocalDateTime dt = LocalDateTime.of(2025, 8, 5, 14, 0);
        event.setDate(dt);

        Calendar cal = new Calendar();
        cal.setId(7L);
        cal.setName("CalEv");
        event.setCalendar(cal);

        assertEquals(30L, event.getId());
        assertEquals("Meeting", event.getTitle());
        assertEquals("DescEv", event.getDescription());
        assertEquals(dt, event.getDate());
        assertSame(cal, event.getCalendar());
    }
}
