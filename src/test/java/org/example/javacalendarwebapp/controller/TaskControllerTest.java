// src/test/java/org/example/javacalendarwebapp/controller/TaskControllerTest.java
package org.example.javacalendarwebapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.javacalendarwebapp.entity.Task;
import org.example.javacalendarwebapp.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Rejestrujemy GlobalExceptionHandler, który obsłuży wyjątki z kontrolera
        mockMvc = MockMvcBuilders.standaloneSetup(taskController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllTasks_ReturnsList() throws Exception {
        Task t1 = new Task(); t1.setId(1L); t1.setTitle("T1");
        Task t2 = new Task(); t2.setId(2L); t2.setTitle("T2");
        when(taskService.findAll()).thenReturn(List.of(t1, t2));

        mockMvc.perform(get("/tasks").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(t1, t2))));
        verify(taskService, times(1)).findAll();
    }

    @Test
    void getTask_Exists_ReturnsTask() throws Exception {
        Task t = new Task(); t.setId(5L); t.setTitle("Existing");
        when(taskService.findById(5L)).thenReturn(Optional.of(t));

        mockMvc.perform(get("/tasks/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(t)));
        verify(taskService, times(1)).findById(5L);
    }

    @Test
    void getTask_NotExists_Returns500() throws Exception {
        when(taskService.findById(10L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/tasks/10").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
        verify(taskService, times(1)).findById(10L);
    }

    @Test
    void createTask_Valid_ReturnsCreatedTask() throws Exception {
        Task in = new Task(); in.setTitle("NewTask");
        Task saved = new Task(); saved.setId(7L); saved.setTitle("NewTask");
        when(taskService.create(any(Task.class))).thenReturn(saved);

        mockMvc.perform(post("/tasks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(in)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(saved)));
        verify(taskService, times(1)).create(any(Task.class));
    }

    @Test
    void updateTask_Exists_ReturnsUpdated() throws Exception {
        Task upd = new Task(); upd.setTitle("Updated");
        Task saved = new Task(); saved.setId(3L); saved.setTitle("Updated");
        when(taskService.update(eq(3L), any(Task.class))).thenReturn(saved);

        mockMvc.perform(put("/tasks/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(upd)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(saved)));
        verify(taskService, times(1)).update(eq(3L), any(Task.class));
    }

    @Test
    void updateTask_NotExists_Returns200Empty() throws Exception {
        when(taskService.update(eq(4L), any(Task.class))).thenReturn(null);

        mockMvc.perform(put("/tasks/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new Task())))
                .andExpect(status().isOk())
                .andExpect(content().string("")); // puste ciało
        verify(taskService, times(1)).update(eq(4L), any(Task.class));
    }

    @Test
    void deleteTask_ReturnsNoContent() throws Exception {
        doNothing().when(taskService).delete(8L);

        mockMvc.perform(delete("/tasks/8"))
                .andExpect(status().isNoContent());
        verify(taskService, times(1)).delete(8L);
    }

    @Test
    void getTaskDate_Exists_ReturnsIsoArray() throws Exception {
        // Jackson domyślnie serializuje LocalDateTime jako tablicę [rok, miesiąc, dzień, godzina, minuta, sekunda]
        LocalDateTime dt = LocalDateTime.of(2025, 6, 3, 15, 0);
        when(taskService.getTaskDateById(11L)).thenReturn(dt);

        mockMvc.perform(get("/tasks/11/date"))
                .andExpect(status().isOk())
                .andExpect(content().string("[2025,6,3,15,0]"));
        verify(taskService, times(1)).getTaskDateById(11L);
    }

    @Test
    void getTaskDate_NotExists_Returns404() throws Exception {
        when(taskService.getTaskDateById(12L)).thenThrow(new IllegalArgumentException("not found"));

        mockMvc.perform(get("/tasks/12/date"))
                .andExpect(status().isNotFound());
        verify(taskService, times(1)).getTaskDateById(12L);
    }

    @Test
    void getCompletedTasks_ReturnsList() throws Exception {
        Task done = new Task(); done.setId(20L); done.setCompleted(true);
        when(taskService.findAllCompletedTasks()).thenReturn(List.of(done));

        mockMvc.perform(get("/tasks/completed"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(done))));
        verify(taskService, times(1)).findAllCompletedTasks();
    }

    @Test
    void getUncompletedTasks_ReturnsList() throws Exception {
        Task notDone = new Task(); notDone.setId(21L); notDone.setCompleted(false);
        when(taskService.findAllUncompletedTasks()).thenReturn(List.of(notDone));

        mockMvc.perform(get("/tasks/uncompleted"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(notDone))));
        verify(taskService, times(1)).findAllUncompletedTasks();
    }

    @Test
    void markTaskAsCompleted_Exists_ReturnsTask() throws Exception {
        Task done = new Task(); done.setId(30L); done.setCompleted(true);
        when(taskService.markAsCompleted(30L)).thenReturn(done);

        mockMvc.perform(put("/tasks/30/complete"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(done)));
        verify(taskService, times(1)).markAsCompleted(30L);
    }

    @Test
    void markTaskAsCompleted_NotExists_Returns404() throws Exception {
        when(taskService.markAsCompleted(31L)).thenThrow(new IllegalArgumentException("not found"));

        mockMvc.perform(put("/tasks/31/complete"))
                .andExpect(status().isNotFound());
        verify(taskService, times(1)).markAsCompleted(31L);
    }
}
