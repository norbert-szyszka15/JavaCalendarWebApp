package org.example.javacalendarwebapp.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalendarEntityTest {

    @Test
    void testCalendarIdAndNameOnly() {
        Calendar cal = new Calendar();
        cal.setId(10L);
        cal.setName("Work");

        assertEquals(10L, cal.getId());
        assertEquals("Work", cal.getName());
    }
}
