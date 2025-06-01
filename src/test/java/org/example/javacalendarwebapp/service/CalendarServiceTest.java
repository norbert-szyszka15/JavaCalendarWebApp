package org.example.javacalendarwebapp.service;

import org.example.javacalendarwebapp.entity.Calendar;
import org.example.javacalendarwebapp.repository.CalendarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalendarServiceTest {

    @Mock
    private CalendarRepository calendarRepository;

    @InjectMocks
    private CalendarService calendarService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ReturnsListOfCalendars() {
        Calendar c1 = new Calendar(); c1.setId(1L); c1.setName("C1");
        Calendar c2 = new Calendar(); c2.setId(2L); c2.setName("C2");
        when(calendarRepository.findAll()).thenReturn(List.of(c1, c2));

        List<Calendar> result = calendarService.findAll();
        assertEquals(2, result.size());
        verify(calendarRepository, times(1)).findAll();
    }

    @Test
    void findById_Exists_ReturnsOptional() {
        Calendar c = new Calendar(); c.setId(5L); c.setName("My");
        when(calendarRepository.findById(5L)).thenReturn(Optional.of(c));

        Optional<Calendar> result = calendarService.findById(5L);
        assertTrue(result.isPresent());
        assertEquals("My", result.get().getName());
        verify(calendarRepository, times(1)).findById(5L);
    }

    @Test
    void findById_NotExists_ReturnsEmpty() {
        when(calendarRepository.findById(10L)).thenReturn(Optional.empty());
        Optional<Calendar> result = calendarService.findById(10L);
        assertFalse(result.isPresent());
        verify(calendarRepository, times(1)).findById(10L);
    }

    @Test
    void create_SavesAndReturnsCalendar() {
        Calendar toSave = new Calendar(); toSave.setName("NewCal");
        Calendar saved = new Calendar(); saved.setId(7L); saved.setName("NewCal");
        when(calendarRepository.save(toSave)).thenReturn(saved);

        Calendar result = calendarService.create(toSave);
        assertNotNull(result);
        assertEquals(7L, result.getId());
        assertEquals("NewCal", result.getName());
        verify(calendarRepository, times(1)).save(toSave);
    }

    @Test
    void update_Exists_ReturnsUpdated() {
        Calendar toUpdate = new Calendar(); toUpdate.setName("Upd");
        when(calendarRepository.existsById(3L)).thenReturn(true);
        Calendar saved = new Calendar(); saved.setId(3L); saved.setName("Upd");
        when(calendarRepository.save(any(Calendar.class))).thenReturn(saved);

        Calendar result = calendarService.update(3L, toUpdate);
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("Upd", result.getName());
        verify(calendarRepository, times(1)).existsById(3L);
        verify(calendarRepository, times(1)).save(toUpdate);
    }

    @Test
    void update_NotExists_ReturnsNull() {
        when(calendarRepository.existsById(4L)).thenReturn(false);
        Calendar toUpdate = new Calendar();
        Calendar result = calendarService.update(4L, toUpdate);
        assertNull(result);
        verify(calendarRepository, times(1)).existsById(4L);
        verify(calendarRepository, never()).save(any());
    }

    @Test
    void delete_Exists_Deletes() {
        when(calendarRepository.existsById(8L)).thenReturn(true);
        calendarService.delete(8L);
        verify(calendarRepository, times(1)).existsById(8L);
        verify(calendarRepository, times(1)).deleteById(8L);
    }

    @Test
    void delete_NotExists_DoesNothing() {
        when(calendarRepository.existsById(9L)).thenReturn(false);
        calendarService.delete(9L);
        verify(calendarRepository, times(1)).existsById(9L);
        verify(calendarRepository, never()).deleteById(anyLong());
    }
}
