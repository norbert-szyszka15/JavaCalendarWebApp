// src/test/java/org/example/javacalendarwebapp/service/TaskServiceTest.java
package org.example.javacalendarwebapp.service;

import org.example.javacalendarwebapp.entity.Task;
import org.example.javacalendarwebapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ReturnsAllTasks() {
        Task t1 = new Task(); t1.setId(1L); t1.setTitle("A");
        Task t2 = new Task(); t2.setId(2L); t2.setTitle("B");
        when(taskRepository.findAll()).thenReturn(List.of(t1, t2));

        List<Task> result = taskService.findAll();
        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void findById_Exists_ReturnsOptional() {
        Task t = new Task(); t.setId(4L); t.setTitle("X");
        when(taskRepository.findById(4L)).thenReturn(Optional.of(t));

        Optional<Task> result = taskService.findById(4L);
        assertTrue(result.isPresent());
        assertEquals("X", result.get().getTitle());
        verify(taskRepository, times(1)).findById(4L);
    }

    @Test
    void findById_NotExists_ReturnsEmpty() {
        when(taskRepository.findById(9L)).thenReturn(Optional.empty());
        Optional<Task> result = taskService.findById(9L);
        assertFalse(result.isPresent());
        verify(taskRepository, times(1)).findById(9L);
    }

    @Test
    void create_SavesAndReturnsTask() {
        Task toSave = new Task(); toSave.setTitle("New");
        Task saved = new Task(); saved.setId(7L); saved.setTitle("New");
        when(taskRepository.save(toSave)).thenReturn(saved);

        Task result = taskService.create(toSave);
        assertEquals(7L, result.getId());
        assertEquals("New", result.getTitle());
        verify(taskRepository, times(1)).save(toSave);
    }

    @Test
    void update_Exists_ReturnsUpdated() {
        Task toUpdate = new Task(); toUpdate.setTitle("Upd");
        when(taskRepository.existsById(2L)).thenReturn(true);
        Task saved = new Task(); saved.setId(2L); saved.setTitle("Upd");
        when(taskRepository.save(any(Task.class))).thenReturn(saved);

        Task result = taskService.update(2L, toUpdate);
        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("Upd", result.getTitle());
        verify(taskRepository, times(1)).existsById(2L);
        verify(taskRepository, times(1)).save(toUpdate);
    }

    @Test
    void update_NotExists_ReturnsNull() {
        when(taskRepository.existsById(3L)).thenReturn(false);
        Task toUpdate = new Task();
        Task result = taskService.update(3L, toUpdate);
        assertNull(result);
        verify(taskRepository, times(1)).existsById(3L);
        verify(taskRepository, never()).save(any());
    }

    @Test
    void delete_Exists_Deletes() {
        when(taskRepository.existsById(5L)).thenReturn(true);
        taskService.delete(5L);
        verify(taskRepository, times(1)).existsById(5L);
        verify(taskRepository, times(1)).deleteById(5L);
    }

    @Test
    void delete_NotExists_DoesNothing() {
        when(taskRepository.existsById(6L)).thenReturn(false);
        taskService.delete(6L);
        verify(taskRepository, times(1)).existsById(6L);
        verify(taskRepository, never()).deleteById(anyLong());
    }

    @Test
    void getTaskDateById_Exists_ReturnsDate() {
        Task t = new Task();
        LocalDateTime dt = LocalDateTime.of(2025, 6, 1, 12, 0);
        t.setDate(dt);
        when(taskRepository.findById(11L)).thenReturn(Optional.of(t));

        LocalDateTime result = taskService.getTaskDateById(11L);
        assertEquals(dt, result);
        verify(taskRepository, times(1)).findById(11L);
    }

    @Test
    void getTaskDateById_NotExists_ThrowsException() {
        when(taskRepository.findById(12L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> taskService.getTaskDateById(12L));
        verify(taskRepository, times(1)).findById(12L);
    }

    @Test
    void findAllCompletedTasks_DelegatesToRepo() {
        Task done = new Task(); done.setCompleted(true);
        when(taskRepository.findByCompletedTrue()).thenReturn(List.of(done));

        List<Task> result = taskService.findAllCompletedTasks();
        assertEquals(1, result.size());
        assertTrue(result.getFirst().getCompleted());
        verify(taskRepository, times(1)).findByCompletedTrue();
    }

    @Test
    void findAllUncompletedTasks_DelegatesToRepo() {
        Task notDone = new Task(); notDone.setCompleted(false);
        when(taskRepository.findByCompletedFalse()).thenReturn(List.of(notDone));

        List<Task> result = taskService.findAllUncompletedTasks();
        assertEquals(1, result.size());
        assertFalse(result.getFirst().getCompleted());
        verify(taskRepository, times(1)).findByCompletedFalse();
    }

    @Test
    void markAsCompleted_Exists_SetsCompletedTrueAndSaves() {
        Task t = new Task(); t.setId(20L); t.setCompleted(false);
        when(taskRepository.findById(20L)).thenReturn(Optional.of(t));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task result = taskService.markAsCompleted(20L);
        assertTrue(result.getCompleted());
        verify(taskRepository, times(1)).findById(20L);
        verify(taskRepository, times(1)).save(t);
    }

    @Test
    void markAsCompleted_NotExists_ThrowsException() {
        when(taskRepository.findById(21L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> taskService.markAsCompleted(21L));
        verify(taskRepository, times(1)).findById(21L);
        verify(taskRepository, never()).save(any());
    }
}
