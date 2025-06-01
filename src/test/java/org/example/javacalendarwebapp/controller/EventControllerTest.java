// src/test/java/org/example/javacalendarwebapp/controller/EventControllerTest.java
package org.example.javacalendarwebapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.javacalendarwebapp.entity.Event;
import org.example.javacalendarwebapp.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(eventController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllEvents_ReturnsList() throws Exception {
        Event e1 = new Event(); e1.setId(1L); e1.setTitle("E1");
        Event e2 = new Event(); e2.setId(2L); e2.setTitle("E2");
        when(eventService.findAll()).thenReturn(List.of(e1, e2));

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(e1, e2))));
        verify(eventService, times(1)).findAll();
    }

    @Test
    void getEvent_Exists_ReturnsEvent() throws Exception {
        Event e = new Event(); e.setId(5L); e.setTitle("Meeting");
        when(eventService.findById(5L)).thenReturn(Optional.of(e));

        mockMvc.perform(get("/events/5"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(e)));
        verify(eventService, times(1)).findById(5L);
    }

    @Test
    void getEvent_NotExists_Returns500() throws Exception {
        when(eventService.findById(10L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/events/10"))
                .andExpect(status().isInternalServerError());
        verify(eventService, times(1)).findById(10L);
    }

    @Test
    void createEvent_Valid_ReturnsCreatedEvent() throws Exception {
        Event input = new Event(); input.setTitle("Conference");
        Event saved = new Event(); saved.setId(7L); saved.setTitle("Conference");
        when(eventService.create(any(Event.class))).thenReturn(saved);

        mockMvc.perform(post("/events/create")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(saved)));
        verify(eventService, times(1)).create(any(Event.class));
    }

    @Test
    void updateEvent_Exists_ReturnsUpdated() throws Exception {
        Event upd = new Event(); upd.setTitle("UpdatedEvent");
        Event saved = new Event(); saved.setId(3L); saved.setTitle("UpdatedEvent");
        when(eventService.update(eq(3L), any(Event.class))).thenReturn(saved);

        mockMvc.perform(put("/events/3")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(upd)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(saved)));
        verify(eventService, times(1)).update(eq(3L), any(Event.class));
    }

    @Test
    void updateEvent_NotExists_Returns200Empty() throws Exception {
        when(eventService.update(eq(4L), any(Event.class))).thenReturn(null);

        mockMvc.perform(put("/events/4")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(new Event())))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
        verify(eventService, times(1)).update(eq(4L), any(Event.class));
    }

    @Test
    void deleteEvent_ReturnsNoContent() throws Exception {
        doNothing().when(eventService).delete(8L);

        mockMvc.perform(delete("/events/8"))
                .andExpect(status().isNoContent());
        verify(eventService, times(1)).delete(8L);
    }

    @Test
    void getEventDate_Exists_ReturnsArray() throws Exception {
        // Jackson serializuje LocalDateTime jako tablicę [rok, miesiąc, dzień, godzina, minuta]
        LocalDateTime dt = LocalDateTime.of(2025, 6, 2, 9, 0);
        when(eventService.getEventDateByID(11L)).thenReturn(dt);

        mockMvc.perform(get("/events/11/date"))
                .andExpect(status().isOk())
                .andExpect(content().string("[2025,6,2,9,0]"));
        verify(eventService, times(1)).getEventDateByID(11L);
    }

    @Test
    void getEventDate_NotExists_Returns404() throws Exception {
        when(eventService.getEventDateByID(12L)).thenThrow(new IllegalArgumentException("not found"));

        mockMvc.perform(get("/events/12/date"))
                .andExpect(status().isNotFound());
        verify(eventService, times(1)).getEventDateByID(12L);
    }
}
