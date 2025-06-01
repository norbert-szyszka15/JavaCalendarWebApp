package org.example.javacalendarwebapp.calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class CalendarServiceTest {

    @Mock
    private CalendarRepository calendarRepository;

    @InjectMocks
    private CalendarService calendarService;

    private Calendar sampleCalendar;

    @BeforeEach
    void setUp() {
        sampleCalendar = new Calendar();
        sampleCalendar.setId(1L);
        sampleCalendar.setName("Sample");
    }

    @Test
    void findAll_shouldReturnListFromRepository() {
        List<Calendar> expectedList = Collections.singletonList(sampleCalendar);
        when(calendarRepository.findAll()).thenReturn(expectedList);

        List<Calendar> result = calendarService.findAll();

        assertThat(result).isSameAs(expectedList);
        verify(calendarRepository, times(1)).findAll();
    }

    @Test
    void findById_whenExists_shouldReturnOptionalWithCalendar() {
        when(calendarRepository.findById(1L)).thenReturn(Optional.of(sampleCalendar));

        Optional<Calendar> result = calendarService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(sampleCalendar);
        verify(calendarRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotExists_shouldReturnEmptyOptional() {
        when(calendarRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Calendar> result = calendarService.findById(2L);

        assertThat(result).isNotPresent();
        verify(calendarRepository, times(1)).findById(2L);
    }

    @Test
    void create_shouldSaveAndReturnCalendar() {
        Calendar toCreate = new Calendar();
        toCreate.setName("New Calendar");

        Calendar saved = new Calendar();
        saved.setId(5L);
        saved.setName("New Calendar");
        when(calendarRepository.save(toCreate)).thenReturn(saved);

        Calendar result = calendarService.create(toCreate);

        assertThat(result.getId()).isEqualTo(5L);
        assertThat(result.getName()).isEqualTo("New Calendar");
        verify(calendarRepository, times(1)).save(toCreate);
    }

    @Test
    void update_whenExists_shouldSetIdAndSave() {
        Long idToUpdate = 10L;
        Calendar incoming = new Calendar();
        incoming.setName("Updated Name");

        when(calendarRepository.existsById(idToUpdate)).thenReturn(true);

        Calendar saved = new Calendar();
        saved.setId(idToUpdate);
        saved.setName("Updated Name");
        ArgumentCaptor<Calendar> captor = ArgumentCaptor.forClass(Calendar.class);
        when(calendarRepository.save(captor.capture())).thenReturn(saved);

        Calendar result = calendarService.update(idToUpdate, incoming);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(idToUpdate);
        assertThat(result.getName()).isEqualTo("Updated Name");

        Calendar passedToSave = captor.getValue();
        assertThat(passedToSave.getId()).isEqualTo(idToUpdate);
        assertThat(passedToSave.getName()).isEqualTo("Updated Name");

        verify(calendarRepository, times(1)).existsById(idToUpdate);
        verify(calendarRepository, times(1)).save(any(Calendar.class));
    }

    @Test
    void update_whenNotExists_shouldReturnNullAndNotSave() {
        Long idToUpdate = 20L;
        Calendar incoming = new Calendar();
        incoming.setName("Won't Be Saved");

        when(calendarRepository.existsById(idToUpdate)).thenReturn(false);

        Calendar result = calendarService.update(idToUpdate, incoming);

        assertThat(result).isNull();
        verify(calendarRepository, never()).save(any(Calendar.class));
        verify(calendarRepository, times(1)).existsById(idToUpdate);
    }

    @Test
    void delete_whenExists_shouldCallDeleteById() {
        Long idToDelete = 7L;
        when(calendarRepository.existsById(idToDelete)).thenReturn(true);

        calendarService.delete(idToDelete);

        verify(calendarRepository, times(1)).existsById(idToDelete);
        verify(calendarRepository, times(1)).deleteById(idToDelete);
    }

    @Test
    void delete_whenNotExists_shouldNotCallDeleteById() {
        Long idToDelete = 8L;
        when(calendarRepository.existsById(idToDelete)).thenReturn(false);

        calendarService.delete(idToDelete);

        verify(calendarRepository, times(1)).existsById(idToDelete);
        verify(calendarRepository, never()).deleteById(anyLong());
    }
}
