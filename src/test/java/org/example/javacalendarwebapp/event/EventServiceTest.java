package org.example.javacalendarwebapp.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private Event sampleEvent;

    @BeforeEach
    void setUp() {
        sampleEvent = new Event();
        sampleEvent.setId(1L);
        sampleEvent.setTitle("Sample");
        sampleEvent.setDescription("Sample description");
        sampleEvent.setDate(LocalDateTime.of(2025, 6, 1, 12, 0));
    }

    @Test
    void findAll_shouldReturnListFromRepository() {
        List<Event> expectedList = Collections.singletonList(sampleEvent);
        when(eventRepository.findAll()).thenReturn(expectedList);

        List<Event> result = eventService.findAll();

        assertThat(result).isSameAs(expectedList);
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    void findById_whenExists_shouldReturnOptionalWithEvent() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(sampleEvent));

        Optional<Event> result = eventService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(sampleEvent);
        verify(eventRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotExists_shouldReturnEmptyOptional() {
        when(eventRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Event> result = eventService.findById(2L);

        assertThat(result).isNotPresent();
        verify(eventRepository, times(1)).findById(2L);
    }

    @Test
    void create_shouldSaveAndReturnEvent() {
        Event toCreate = new Event();
        toCreate.setTitle("New Event");
        toCreate.setDescription("Desc");
        toCreate.setDate(LocalDateTime.of(2025, 7, 1, 10, 30));

        Event saved = new Event();
        saved.setId(5L);
        saved.setTitle("New Event");
        saved.setDescription("Desc");
        saved.setDate(LocalDateTime.of(2025, 7, 1, 10, 30));
        when(eventRepository.save(toCreate)).thenReturn(saved);

        Event result = eventService.create(toCreate);

        assertThat(result.getId()).isEqualTo(5L);
        assertThat(result.getTitle()).isEqualTo("New Event");
        assertThat(result.getDescription()).isEqualTo("Desc");
        assertThat(result.getDate()).isEqualTo(LocalDateTime.of(2025, 7, 1, 10, 30));
        verify(eventRepository, times(1)).save(toCreate);
    }

    @Test
    void update_whenExists_shouldSetIdAndSave() {
        Long idToUpdate = 10L;
        Event incoming = new Event();
        incoming.setTitle("Updated Title");
        incoming.setDescription("Updated Desc");
        incoming.setDate(LocalDateTime.of(2025, 8, 2, 14, 0));

        when(eventRepository.existsById(idToUpdate)).thenReturn(true);
        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);

        Event saved = new Event();
        saved.setId(idToUpdate);
        saved.setTitle("Updated Title");
        saved.setDescription("Updated Desc");
        saved.setDate(LocalDateTime.of(2025, 8, 2, 14, 0));
        when(eventRepository.save(captor.capture())).thenReturn(saved);

        Event result = eventService.update(idToUpdate, incoming);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(idToUpdate);
        assertThat(result.getTitle()).isEqualTo("Updated Title");
        assertThat(result.getDescription()).isEqualTo("Updated Desc");
        assertThat(result.getDate()).isEqualTo(LocalDateTime.of(2025, 8, 2, 14, 0));

        Event passed = captor.getValue();
        assertThat(passed.getId()).isEqualTo(idToUpdate);
        assertThat(passed.getTitle()).isEqualTo("Updated Title");
        assertThat(passed.getDescription()).isEqualTo("Updated Desc");
        assertThat(passed.getDate()).isEqualTo(LocalDateTime.of(2025, 8, 2, 14, 0));

        verify(eventRepository, times(1)).existsById(idToUpdate);
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void update_whenNotExists_shouldReturnNullAndNotSave() {
        Long idToUpdate = 20L;
        Event incoming = new Event();
        incoming.setTitle("Won't Be Saved");
        incoming.setDescription("Desc");
        incoming.setDate(LocalDateTime.of(2025, 9, 3, 9, 15));

        when(eventRepository.existsById(idToUpdate)).thenReturn(false);

        Event result = eventService.update(idToUpdate, incoming);

        assertThat(result).isNull();
        verify(eventRepository, times(1)).existsById(idToUpdate);
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void delete_whenExists_shouldCallDeleteById() {
        Long idToDelete = 7L;
        when(eventRepository.existsById(idToDelete)).thenReturn(true);

        eventService.delete(idToDelete);

        verify(eventRepository, times(1)).existsById(idToDelete);
        verify(eventRepository, times(1)).deleteById(idToDelete);
    }

    @Test
    void delete_whenNotExists_shouldNotCallDeleteById() {
        Long idToDelete = 8L;
        when(eventRepository.existsById(idToDelete)).thenReturn(false);

        eventService.delete(idToDelete);

        verify(eventRepository, times(1)).existsById(idToDelete);
        verify(eventRepository, never()).deleteById(anyLong());
    }

    @Test
    void getEventDateByID_whenExists_shouldReturnDate() {
        Long id = 30L;
        Event e = new Event();
        e.setId(id);
        e.setDate(LocalDateTime.of(2025, 9, 3, 9, 15));

        when(eventRepository.findById(id)).thenReturn(Optional.of(e));

        LocalDateTime result = eventService.getEventDateByID(id);

        assertThat(result).isEqualTo(LocalDateTime.of(2025, 9, 3, 9, 15));
        verify(eventRepository, times(1)).findById(id);
    }

    @Test
    void getEventDateByID_whenNotExists_shouldThrowIllegalArgument() {
        Long id = 40L;
        when(eventRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.getEventDateByID(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Event not found with id: " + id);

        verify(eventRepository, times(1)).findById(id);
    }

    @Test
    void getEventDateByID_whenDateIsNull_shouldReturnNull() {
        Long id = 50L;
        Event e = new Event();
        e.setId(id);
        e.setDate(null);

        when(eventRepository.findById(id)).thenReturn(Optional.of(e));

        LocalDateTime result = eventService.getEventDateByID(id);

        assertThat(result).isNull();
        verify(eventRepository, times(1)).findById(id);
    }
}
