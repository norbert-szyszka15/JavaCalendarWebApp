package org.example.javacalendarwebapp.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task sampleTask;

    @BeforeEach
    void setUp() {
        sampleTask = new Task();
        sampleTask.setId(1L);
        sampleTask.setTitle("Sample Task");
        sampleTask.setDescription("Sample Description");
        sampleTask.setDate(LocalDateTime.of(2025, 6, 1, 12, 0));
        sampleTask.setCompleted(false);
    }

    @Test
    void findAll_shouldReturnListFromRepository() {
        List<Task> expected = Collections.singletonList(sampleTask);
        when(taskRepository.findAll()).thenReturn(expected);

        List<Task> result = taskService.findAll();

        assertThat(result).isSameAs(expected);
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void findById_whenExists_shouldReturnOptionalWithTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));

        Optional<Task> result = taskService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(sampleTask);
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotExists_shouldReturnEmptyOptional() {
        when(taskRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Task> result = taskService.findById(2L);

        assertThat(result).isNotPresent();
        verify(taskRepository, times(1)).findById(2L);
    }

    @Test
    void create_shouldSaveAndReturnTask() {
        Task toCreate = new Task();
        toCreate.setTitle("New Task");
        toCreate.setDescription("Desc");
        toCreate.setDate(LocalDateTime.of(2025, 7, 1, 10, 30));

        Task saved = new Task();
        saved.setId(5L);
        saved.setTitle("New Task");
        saved.setDescription("Desc");
        saved.setDate(LocalDateTime.of(2025, 7, 1, 10, 30));

        when(taskRepository.save(toCreate)).thenReturn(saved);

        Task result = taskService.create(toCreate);

        assertThat(result.getId()).isEqualTo(5L);
        assertThat(result.getTitle()).isEqualTo("New Task");
        assertThat(result.getDescription()).isEqualTo("Desc");
        assertThat(result.getDate()).isEqualTo(LocalDateTime.of(2025, 7, 1, 10, 30));
        verify(taskRepository, times(1)).save(toCreate);
    }

    @Test
    void update_whenExists_shouldSetIdAndSave() {
        Long idToUpdate = 10L;
        Task incoming = new Task();
        incoming.setTitle("Updated Title");
        incoming.setDescription("Updated Desc");
        incoming.setDate(LocalDateTime.of(2025, 8, 2, 14, 0));
        incoming.setCompleted(false);

        when(taskRepository.existsById(idToUpdate)).thenReturn(true);
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);

        Task saved = new Task();
        saved.setId(idToUpdate);
        saved.setTitle("Updated Title");
        saved.setDescription("Updated Desc");
        saved.setDate(LocalDateTime.of(2025, 8, 2, 14, 0));
        saved.setCompleted(false);
        when(taskRepository.save(captor.capture())).thenReturn(saved);

        Task result = taskService.update(idToUpdate, incoming);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(idToUpdate);
        assertThat(result.getTitle()).isEqualTo("Updated Title");
        assertThat(result.getDescription()).isEqualTo("Updated Desc");
        assertThat(result.getDate()).isEqualTo(LocalDateTime.of(2025, 8, 2, 14, 0));
        assertThat(result.getCompleted()).isFalse();

        Task passed = captor.getValue();
        assertThat(passed.getId()).isEqualTo(idToUpdate);
        assertThat(passed.getTitle()).isEqualTo("Updated Title");
        assertThat(passed.getDescription()).isEqualTo("Updated Desc");
        assertThat(passed.getDate()).isEqualTo(LocalDateTime.of(2025, 8, 2, 14, 0));
        assertThat(passed.getCompleted()).isFalse();

        verify(taskRepository, times(1)).existsById(idToUpdate);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void update_whenNotExists_shouldReturnNullAndNotSave() {
        Long idToUpdate = 20L;
        Task incoming = new Task();
        incoming.setTitle("Won't Be Saved");
        incoming.setDescription("Desc");
        incoming.setDate(LocalDateTime.of(2025, 9, 3, 9, 15));

        when(taskRepository.existsById(idToUpdate)).thenReturn(false);

        Task result = taskService.update(idToUpdate, incoming);

        assertThat(result).isNull();
        verify(taskRepository, times(1)).existsById(idToUpdate);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void delete_whenExists_shouldCallDeleteById() {
        Long idToDelete = 7L;
        when(taskRepository.existsById(idToDelete)).thenReturn(true);

        taskService.delete(idToDelete);

        verify(taskRepository, times(1)).existsById(idToDelete);
        verify(taskRepository, times(1)).deleteById(idToDelete);
    }

    @Test
    void delete_whenNotExists_shouldNotCallDeleteById() {
        Long idToDelete = 8L;
        when(taskRepository.existsById(idToDelete)).thenReturn(false);

        taskService.delete(idToDelete);

        verify(taskRepository, times(1)).existsById(idToDelete);
        verify(taskRepository, never()).deleteById(anyLong());
    }

    @Test
    void getTaskDateById_whenExists_shouldReturnDate() {
        Long id = 30L;
        Task t = new Task();
        t.setId(id);
        t.setDate(LocalDateTime.of(2025, 9, 3, 9, 15));

        when(taskRepository.findById(id)).thenReturn(Optional.of(t));

        LocalDateTime result = taskService.getTaskDateById(id);

        assertThat(result).isEqualTo(LocalDateTime.of(2025, 9, 3, 9, 15));
        verify(taskRepository, times(1)).findById(id);
    }

    @Test
    void getTaskDateById_whenNotExists_shouldThrowIllegalArgument() {
        Long id = 40L;
        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTaskDateById(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Task not found with id: " + id);

        verify(taskRepository, times(1)).findById(id);
    }

    @Test
    void getTaskDateById_whenDateIsNull_shouldReturnNull() {
        Long id = 50L;
        Task t = new Task();
        t.setId(id);
        t.setDate(null);

        when(taskRepository.findById(id)).thenReturn(Optional.of(t));

        LocalDateTime result = taskService.getTaskDateById(id);

        assertThat(result).isNull();
        verify(taskRepository, times(1)).findById(id);
    }

    @Test
    void findAllCompletedTasks_shouldReturnList() {
        Task t1 = new Task();
        t1.setId(2L);
        t1.setCompleted(true);
        Task t2 = new Task();
        t2.setId(3L);
        t2.setCompleted(true);
        List<Task> completed = Arrays.asList(t1, t2);

        when(taskRepository.findByCompletedTrue()).thenReturn(completed);

        List<Task> result = taskService.findAllCompletedTasks();

        assertThat(result).hasSize(2)
                .extracting(Task::getId)
                .containsExactlyInAnyOrder(2L, 3L);
        verify(taskRepository, times(1)).findByCompletedTrue();
    }

    @Test
    void findAllUncompletedTasks_shouldReturnList() {
        Task t1 = new Task();
        t1.setId(4L);
        t1.setCompleted(false);
        Task t2 = new Task();
        t2.setId(5L);
        t2.setCompleted(false);
        List<Task> uncompleted = Arrays.asList(t1, t2);

        when(taskRepository.findByCompletedFalse()).thenReturn(uncompleted);

        List<Task> result = taskService.findAllUncompletedTasks();

        assertThat(result).hasSize(2)
                .extracting(Task::getId)
                .containsExactlyInAnyOrder(4L, 5L);
        verify(taskRepository, times(1)).findByCompletedFalse();
    }

    @Test
    void markAsCompleted_whenExists_shouldSetCompletedTrueAndSave() {
        Long idToMark = 80L;
        Task t = new Task();
        t.setId(idToMark);
        t.setCompleted(false);

        when(taskRepository.findById(idToMark)).thenReturn(Optional.of(t));
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);

        Task updated = new Task();
        updated.setId(idToMark);
        updated.setCompleted(true);
        when(taskRepository.save(captor.capture())).thenReturn(updated);

        Task result = taskService.markAsCompleted(idToMark);

        assertThat(result.getId()).isEqualTo(idToMark);
        assertThat(result.getCompleted()).isTrue();

        Task passed = captor.getValue();
        assertThat(passed.getId()).isEqualTo(idToMark);
        assertThat(passed.getCompleted()).isTrue();

        verify(taskRepository, times(1)).findById(idToMark);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void markAsCompleted_whenNotExists_shouldThrowIllegalArgument() {
        Long idToMark = 90L;
        when(taskRepository.findById(idToMark)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.markAsCompleted(idToMark))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Task not found with id: " + idToMark);

        verify(taskRepository, times(1)).findById(idToMark);
        verify(taskRepository, never()).save(any(Task.class));
    }
}
