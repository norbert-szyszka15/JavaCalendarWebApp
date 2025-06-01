package org.example.javacalendarwebapp.task;

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

class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

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

        mockMvc = MockMvcBuilders.standaloneSetup(taskController)
                .setControllerAdvice(new TaskControllerTest.GlobalExceptionHandler())
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
    void getAllTasks_shouldReturnListOfTasks() throws Exception {
        Task t1 = new Task();
        t1.setId(1L);
        t1.setTitle("Task One");
        Task t2 = new Task();
        t2.setId(2L);
        t2.setTitle("Task Two");
        List<Task> tasks = Arrays.asList(t1, t2);

        when(taskService.findAll()).thenReturn(tasks);

        mockMvc.perform(get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Task One"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Task Two"));

        verify(taskService, times(1)).findAll();
    }

    @Test
    void getTaskById_whenExists_shouldReturnTask() throws Exception {
        Task t = new Task();
        t.setId(10L);
        t.setTitle("Test Task");
        t.setDescription("Desc");
        t.setDate(LocalDateTime.of(2025, 6, 1, 12, 0));

        when(taskService.findById(10L)).thenReturn(Optional.of(t));

        mockMvc.perform(get("/tasks/{id}", 10L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Desc"))
                .andExpect(jsonPath("$.date").value("2025-06-01T12:00:00"));

        verify(taskService, times(1)).findById(10L);
    }

    @Test
    void getTaskById_whenNotFound_shouldReturn404() throws Exception {
        when(taskService.findById(5L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/tasks/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("\"Task not found with id: 5\""));

        verify(taskService, times(1)).findById(5L);
    }

    @Test
    void createTask_shouldReturnCreatedTask() throws Exception {
        Task input = new Task();
        input.setTitle("New Task");
        input.setDescription("Desc");
        input.setDate(LocalDateTime.of(2025, 7, 1, 10, 30));

        Task saved = new Task();
        saved.setId(100L);
        saved.setTitle("New Task");
        saved.setDescription("Desc");
        saved.setDate(LocalDateTime.of(2025, 7, 1, 10, 30));

        when(taskService.create(any(Task.class))).thenReturn(saved);

        mockMvc.perform(post("/tasks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.description").value("Desc"))
                .andExpect(jsonPath("$.date").value("2025-07-01T10:30:00"));

        verify(taskService, times(1)).create(any(Task.class));
    }

    @Test
    void updateTask_shouldReturnUpdatedTask() throws Exception {
        Long idToUpdate = 50L;
        Task input = new Task();
        input.setTitle("Updated Title");
        input.setDescription("Updated Desc");
        input.setDate(LocalDateTime.of(2025, 8, 2, 14, 0));

        Task updated = new Task();
        updated.setId(idToUpdate);
        updated.setTitle("Updated Title");
        updated.setDescription("Updated Desc");
        updated.setDate(LocalDateTime.of(2025, 8, 2, 14, 0));

        when(taskService.update(eq(idToUpdate), any(Task.class))).thenReturn(updated);

        mockMvc.perform(put("/tasks/{id}", idToUpdate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(50))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.description").value("Updated Desc"))
                .andExpect(jsonPath("$.date").value("2025-08-02T14:00:00"));

        verify(taskService, times(1)).update(eq(idToUpdate), any(Task.class));
    }

    @Test
    void deleteTask_shouldReturnNoContent() throws Exception {
        doNothing().when(taskService).delete(77L);

        mockMvc.perform(delete("/tasks/{id}", 77L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).delete(77L);
    }

    @Test
    void getTaskDateById_whenDateExists_shouldReturnDate() throws Exception {
        LocalDateTime dt = LocalDateTime.of(2025, 9, 3, 9, 15);
        when(taskService.getTaskDateById(30L)).thenReturn(dt);

        mockMvc.perform(get("/tasks/{id}/date", 30L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("\"2025-09-03T09:15:00\""));

        verify(taskService, times(1)).getTaskDateById(30L);
    }

    @Test
    void getTaskDateById_whenDateNull_shouldReturnNoContent() throws Exception {
        when(taskService.getTaskDateById(40L)).thenReturn(null);

        mockMvc.perform(get("/tasks/{id}/date", 40L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).getTaskDateById(40L);
    }

    @Test
    void getTaskDateById_whenThrowsIllegalArgument_shouldReturn404() throws Exception {
        when(taskService.getTaskDateById(60L)).thenThrow(new IllegalArgumentException());

        mockMvc.perform(get("/tasks/{id}/date", 60L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).getTaskDateById(60L);
    }

    @Test
    void getAllCompletedTasks_shouldReturnList() throws Exception {
        Task t1 = new Task();
        t1.setId(2L);
        t1.setCompleted(true);
        Task t2 = new Task();
        t2.setId(3L);
        t2.setCompleted(true);
        List<Task> completed = Arrays.asList(t1, t2);

        when(taskService.findAllCompletedTasks()).thenReturn(completed);

        mockMvc.perform(get("/tasks/completed")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].completed").value(true))
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath("$[1].completed").value(true));

        verify(taskService, times(1)).findAllCompletedTasks();
    }

    @Test
    void getAllUncompletedTasks_shouldReturnList() throws Exception {
        Task t1 = new Task();
        t1.setId(4L);
        t1.setCompleted(false);
        Task t2 = new Task();
        t2.setId(5L);
        t2.setCompleted(false);
        List<Task> uncompleted = Arrays.asList(t1, t2);

        when(taskService.findAllUncompletedTasks()).thenReturn(uncompleted);

        mockMvc.perform(get("/tasks/uncompleted")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(4))
                .andExpect(jsonPath("$[0].completed").value(false))
                .andExpect(jsonPath("$[1].id").value(5))
                .andExpect(jsonPath("$[1].completed").value(false));

        verify(taskService, times(1)).findAllUncompletedTasks();
    }

    @Test
    void markTaskAsCompleted_whenExists_shouldReturnUpdatedTask() throws Exception {
        Long idToMark = 80L;
        Task updated = new Task();
        updated.setId(idToMark);
        updated.setCompleted(true);

        when(taskService.markAsCompleted(idToMark)).thenReturn(updated);

        mockMvc.perform(put("/tasks/{id}/complete", idToMark)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(80))
                .andExpect(jsonPath("$.completed").value(true));

        verify(taskService, times(1)).markAsCompleted(idToMark);
    }

    @Test
    void markTaskAsCompleted_whenNotExists_shouldReturn404() throws Exception {
        Long idToMark = 90L;
        when(taskService.markAsCompleted(idToMark)).thenThrow(new IllegalArgumentException());

        mockMvc.perform(put("/tasks/{id}/complete", idToMark)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).markAsCompleted(idToMark);
    }
}
