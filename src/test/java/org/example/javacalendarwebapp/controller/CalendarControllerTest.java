package org.example.javacalendarwebapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.javacalendarwebapp.entity.Calendar;
import org.example.javacalendarwebapp.service.CalendarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CalendarControllerTest {

    @Mock
    private CalendarService calendarService;

    @InjectMocks
    private CalendarController calendarController;

    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(calendarController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllCalendars_ReturnsList() throws Exception {
        Calendar c1 = new Calendar(); c1.setId(1L); c1.setName("Work");
        Calendar c2 = new Calendar(); c2.setId(2L); c2.setName("Personal");
        when(calendarService.findAll()).thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/calendars")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(c1, c2))));
        verify(calendarService, times(1)).findAll();
    }

    @Test
    void getCalendar_Exists_ReturnsCalendar() throws Exception {
        Calendar c = new Calendar(); c.setId(5L); c.setName("Events");
        when(calendarService.findById(5L)).thenReturn(Optional.of(c));

        mockMvc.perform(get("/calendars/5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(c)));
        verify(calendarService, times(1)).findById(5L);
    }

    @Test
    void getCalendar_NotExists_Returns500() throws Exception {
        when(calendarService.findById(10L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/calendars/10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
        verify(calendarService, times(1)).findById(10L);
    }

    @Test
    void createCalendar_Valid_ReturnsCreatedCalendar() throws Exception {
        Calendar input = new Calendar(); input.setName("NewCal");
        Calendar saved = new Calendar(); saved.setId(7L); saved.setName("NewCal");
        when(calendarService.create(any(Calendar.class))).thenReturn(saved);

        mockMvc.perform(post("/calendars/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(saved)));
        verify(calendarService, times(1)).create(any(Calendar.class));
    }

    @Test
    void updateCalendar_Exists_ReturnsUpdated() throws Exception {
        Calendar upd = new Calendar(); upd.setName("UpdatedCal");
        Calendar saved = new Calendar(); saved.setId(3L); saved.setName("UpdatedCal");
        when(calendarService.update(eq(3L), any(Calendar.class))).thenReturn(saved);

        mockMvc.perform(put("/calendars/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(upd)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(saved)));
        verify(calendarService, times(1)).update(eq(3L), any(Calendar.class));
    }

    @Test
    void updateCalendar_NotExists_Returns200Empty() throws Exception {
        when(calendarService.update(eq(4L), any(Calendar.class))).thenReturn(null);

        mockMvc.perform(put("/calendars/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new Calendar())))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
        verify(calendarService, times(1)).update(eq(4L), any(Calendar.class));
    }

    @Test
    void deleteCalendar_ReturnsNoContent() throws Exception {
        doNothing().when(calendarService).delete(8L);

        mockMvc.perform(delete("/calendars/8"))
                .andExpect(status().isNoContent());
        verify(calendarService, times(1)).delete(8L);
    }
}
