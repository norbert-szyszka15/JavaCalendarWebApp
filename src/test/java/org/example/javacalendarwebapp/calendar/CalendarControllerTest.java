package org.example.javacalendarwebapp.calendar;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CalendarControllerTest {

    @InjectMocks
    private CalendarController calendarController;

    @Mock
    private CalendarService calendarService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(calendarController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @org.springframework.web.bind.annotation.RestControllerAdvice
    static class GlobalExceptionHandler {
        @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
        public ResponseEntity<String> handleNotFound(RuntimeException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        }
    }

    @Test
    void getAllCalendars_shouldReturnListOfCalendars() throws Exception {
        Calendar cal1 = new Calendar();
        cal1.setId(1L);
        cal1.setName("Work Calendar");
        Calendar cal2 = new Calendar();
        cal2.setId(2L);
        cal2.setName("Personal Calendar");

        List<Calendar> calendars = Arrays.asList(cal1, cal2);
        when(calendarService.findAll()).thenReturn(calendars);

        mockMvc.perform(get("/calendars")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Work Calendar"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Personal Calendar"));

        verify(calendarService, times(1)).findAll();
    }

    @Test
    void getCalendarById_whenExists_shouldReturnCalendar() throws Exception {
        Calendar cal = new Calendar();
        cal.setId(10L);
        cal.setName("Test Calendar");

        when(calendarService.findById(10L)).thenReturn(Optional.of(cal));

        mockMvc.perform(get("/calendars/{id}", 10L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Test Calendar"));

        verify(calendarService, times(1)).findById(10L);
    }

    @Test
    void getCalendarById_whenNotFound_shouldReturn404() throws Exception {
        when(calendarService.findById(5L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/calendars/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Event not found with id: 5"));

        verify(calendarService, times(1)).findById(5L);
    }

    @Test
    void createCalendar_shouldReturnCreatedCalendar() throws Exception {
        Calendar input = new Calendar();
        input.setName("New Calendar");

        Calendar saved = new Calendar();
        saved.setId(100L);
        saved.setName("New Calendar");

        when(calendarService.create(Mockito.any(Calendar.class))).thenReturn(saved);

        mockMvc.perform(post("/calendars/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.name").value("New Calendar"));

        verify(calendarService, times(1)).create(Mockito.any(Calendar.class));
    }

    @Test
    void updateCalendar_shouldReturnUpdatedCalendar() throws Exception {
        Long idToUpdate = 42L;
        Calendar input = new Calendar();
        input.setName("Updated Name");

        Calendar updated = new Calendar();
        updated.setId(idToUpdate);
        updated.setName("Updated Name");

        when(calendarService.update(eq(idToUpdate), Mockito.any(Calendar.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/calendars/{id}", idToUpdate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.name").value("Updated Name"));

        verify(calendarService, times(1)).update(eq(idToUpdate), Mockito.any(Calendar.class));
    }

    @Test
    void deleteCalendar_shouldReturnNoContent() throws Exception {
        doNothing().when(calendarService).delete(77L);

        mockMvc.perform(delete("/calendars/{id}", 77L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(calendarService, times(1)).delete(77L);
    }
}
