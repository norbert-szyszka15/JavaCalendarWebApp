package org.example.javacalendarwebapp.service;

import org.example.javacalendarwebapp.entity.Event;
import org.example.javacalendarwebapp.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ReturnsAllEvents() {
        Event e1 = new Event(); e1.setId(1L); e1.setTitle("E1");
        Event e2 = new Event(); e2.setId(2L); e2.setTitle("E2");
        when(eventRepository.findAll()).thenReturn(List.of(e1, e2));

        List<Event> result = eventService.findAll();
        assertEquals(2, result.size());
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    void findById_Exists_ReturnsOptional() {
        Event e = new Event(); e.setId(7L); e.setTitle("Evt");
        when(eventRepository.findById(7L)).thenReturn(Optional.of(e));

        Optional<Event> result = eventService.findById(7L);
        assertTrue(result.isPresent());
        assertEquals("Evt", result.get().getTitle());
        verify(eventRepository, times(1)).findById(7L);
    }

    @Test
    void findById_NotExists_ReturnsEmpty() {
        when(eventRepository.findById(9L)).thenReturn(Optional.empty());
        Optional<Event> result = eventService.findById(9L);
        assertFalse(result.isPresent());
        verify(eventRepository, times(1)).findById(9L);
    }

    @Test
    void create_SavesAndReturnsEvent() {
        Event toSave = new Event(); toSave.setTitle("NewEvt");
        Event saved = new Event(); saved.setId(11L); saved.setTitle("NewEvt");
        when(eventRepository.save(toSave)).thenReturn(saved);

        Event result = eventService.create(toSave);
        assertEquals(11L, result.getId());
        assertEquals("NewEvt", result.getTitle());
        verify(eventRepository, times(1)).save(toSave);
    }

    @Test
    void update_Exists_ReturnsUpdated() {
        Event toUpdate = new Event(); toUpdate.setTitle("UpdEvt");
        when(eventRepository.existsById(5L)).thenReturn(true);
        Event saved = new Event(); saved.setId(5L); saved.setTitle("UpdEvt");
        when(eventRepository.save(any(Event.class))).thenReturn(saved);

        Event result = eventService.update(5L, toUpdate);
        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals("UpdEvt", result.getTitle());
        verify(eventRepository, times(1)).existsById(5L);
        verify(eventRepository, times(1)).save(toUpdate);
    }

    @Test
    void update_NotExists_ReturnsNull() {
        when(eventRepository.existsById(6L)).thenReturn(false);
        Event result = eventService.update(6L, new Event());
        assertNull(result);
        verify(eventRepository, times(1)).existsById(6L);
        verify(eventRepository, never()).save(any());
    }

    @Test
    void delete_Exists_Deletes() {
        when(eventRepository.existsById(4L)).thenReturn(true);
        eventService.delete(4L);
        verify(eventRepository, times(1)).existsById(4L);
        verify(eventRepository, times(1)).deleteById(4L);
    }

    @Test
    void delete_NotExists_DoesNothing() {
        when(eventRepository.existsById(8L)).thenReturn(false);
        eventService.delete(8L);
        verify(eventRepository, times(1)).existsById(8L);
        verify(eventRepository, never()).deleteById(anyLong());
    }

    @Test
    void getEventDateByID_Exists_ReturnsDate() {
        Event e = new Event();
        LocalDateTime dt = LocalDateTime.of(2025, 6, 2, 9, 0);
        e.setDate(dt);
        when(eventRepository.findById(13L)).thenReturn(Optional.of(e));

        LocalDateTime result = eventService.getEventDateByID(13L);
        assertEquals(dt, result);
        verify(eventRepository, times(1)).findById(13L);
    }

    @Test
    void getEventDateByID_NotExists_ThrowsException() {
        when(eventRepository.findById(14L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> eventService.getEventDateByID(14L));
        verify(eventRepository, times(1)).findById(14L);
    }
}
