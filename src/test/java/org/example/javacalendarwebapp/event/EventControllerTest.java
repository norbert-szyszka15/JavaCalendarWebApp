package org.example.javacalendarwebapp.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EventControllerTest {

    @InjectMocks
    private EventController eventController;

    @Mock
    private EventService eventService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        MappingJackson2HttpMessageConverter jacksonConverter =
                new MappingJackson2HttpMessageConverter(objectMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(eventController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(jacksonConverter)
                .build();
    }

    @org.springframework.web.bind.annotation.RestControllerAdvice
    static class GlobalExceptionHandler {
        @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
        public ResponseEntity<String> handleNotFound(RuntimeException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        }
        @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        }
    }

    @Test
    void getAllEvents_shouldReturnListOfEvents() throws Exception {
        Event e1 = new Event();
        e1.setId(1L);
        e1.setTitle("Event One");
        Event e2 = new Event();
        e2.setId(2L);
        e2.setTitle("Event Two");

        List<Event> events = Arrays.asList(e1, e2);
        when(eventService.findAll()).thenReturn(events);

        mockMvc.perform(get("/events")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Event One"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Event Two"));

        verify(eventService, times(1)).findAll();
    }

    @Test
    void getEventById_whenExists_shouldReturnEvent() throws Exception {
        Event e = new Event();
        e.setId(10L);
        e.setTitle("Test Event");
        e.setDescription("Some description");
        e.setDate(LocalDateTime.of(2025, 6, 1, 12, 0));

        when(eventService.findById(10L)).thenReturn(Optional.of(e));

        mockMvc.perform(get("/events/{id}", 10L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.title").value("Test Event"))
                .andExpect(jsonPath("$.description").value("Some description"))
                .andExpect(jsonPath("$.date").value("2025-06-01T12:00:00"));

        verify(eventService, times(1)).findById(10L);
    }

    @Test
    void getEventById_whenNotFound_shouldReturn404() throws Exception {
        when(eventService.findById(5L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/events/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("\"Event not found with id: 5\""));

        verify(eventService, times(1)).findById(5L);
    }

    @Test
    void createEvent_shouldReturnCreatedEvent() throws Exception {
        Event input = new Event();
        input.setTitle("New Event");
        input.setDescription("Desc");
        input.setDate(LocalDateTime.of(2025, 7, 1, 10, 30));

        Event saved = new Event();
        saved.setId(100L);
        saved.setTitle("New Event");
        saved.setDescription("Desc");
        saved.setDate(LocalDateTime.of(2025, 7, 1, 10, 30));

        when(eventService.create(any(Event.class))).thenReturn(saved);

        mockMvc.perform(post("/events/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.title").value("New Event"))
                .andExpect(jsonPath("$.description").value("Desc"))
                .andExpect(jsonPath("$.date").value("2025-07-01T10:30:00"));

        verify(eventService, times(1)).create(any(Event.class));
    }

    @Test
    void updateEvent_shouldReturnUpdatedEvent() throws Exception {
        Long idToUpdate = 50L;
        Event input = new Event();
        input.setTitle("Updated Title");
        input.setDescription("Updated Desc");
        input.setDate(LocalDateTime.of(2025, 8, 2, 14, 0));

        Event updated = new Event();
        updated.setId(idToUpdate);
        updated.setTitle("Updated Title");
        updated.setDescription("Updated Desc");
        updated.setDate(LocalDateTime.of(2025, 8, 2, 14, 0));

        when(eventService.update(eq(idToUpdate), any(Event.class))).thenReturn(updated);

        mockMvc.perform(put("/events/{id}", idToUpdate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(50))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.description").value("Updated Desc"))
                .andExpect(jsonPath("$.date").value("2025-08-02T14:00:00"));

        verify(eventService, times(1)).update(eq(idToUpdate), any(Event.class));
    }

    @Test
    void deleteEvent_shouldReturnNoContent() throws Exception {
        doNothing().when(eventService).delete(77L);

        mockMvc.perform(delete("/events/{id}", 77L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(eventService, times(1)).delete(77L);
    }

    @Test
    void getEventDateById_whenDateExists_shouldReturnDate() throws Exception {
        LocalDateTime dt = LocalDateTime.of(2025, 9, 3, 9, 15);
        when(eventService.getEventDateByID(30L)).thenReturn(dt);

        mockMvc.perform(get("/events/{id}/date", 30L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("\"2025-09-03T09:15:00\""));

        verify(eventService, times(1)).getEventDateByID(30L);
    }

    @Test
    void getEventDateById_whenDateNull_shouldReturnNoContent() throws Exception {
        when(eventService.getEventDateByID(40L)).thenReturn(null);

        mockMvc.perform(get("/events/{id}/date", 40L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(eventService, times(1)).getEventDateByID(40L);
    }

    @Test
    void getEventDateById_whenThrowsIllegalArgument_shouldReturn404() throws Exception {
        when(eventService.getEventDateByID(60L)).thenThrow(new IllegalArgumentException());

        mockMvc.perform(get("/events/{id}/date", 60L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(eventService, times(1)).getEventDateByID(60L);
    }
}
