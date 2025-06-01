package org.example.javacalendarwebapp.user;

import org.example.javacalendarwebapp.calendar.Calendar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setUsername("john");
        sampleUser.setPassword("pass");
        sampleUser.setRoles(new HashSet<>(Collections.singleton("ROLE_USER")));
        Calendar cal = new Calendar();
        cal.setId(5L);
        cal.setName("Work");
        sampleUser.setCalendars(new HashSet<>(Collections.singleton(cal)));
    }

    @Test
    void findAll_shouldReturnListFromRepository() {
        List<User> expected = Collections.singletonList(sampleUser);
        when(userRepository.findAll()).thenReturn(expected);

        List<User> result = userService.findAll();

        assertThat(result).isSameAs(expected);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void findById_whenExists_shouldReturnOptionalWithUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        Optional<User> result = userService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(sampleUser);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotExists_shouldReturnEmptyOptional() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<User> result = userService.findById(2L);

        assertThat(result).isNotPresent();
        verify(userRepository, times(1)).findById(2L);
    }

    @Test
    void create_shouldSaveAndReturnUser() {
        User toCreate = new User();
        toCreate.setUsername("alice");
        toCreate.setPassword("pwd");
        toCreate.setRoles(new HashSet<>(Arrays.asList("ROLE_USER", "ROLE_ADMIN")));

        User saved = new User();
        saved.setId(10L);
        saved.setUsername("alice");
        saved.setPassword("pwd");
        saved.setRoles(new HashSet<>(Arrays.asList("ROLE_USER", "ROLE_ADMIN")));

        when(userRepository.save(toCreate)).thenReturn(saved);

        User result = userService.create(toCreate);

        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getUsername()).isEqualTo("alice");
        assertThat(result.getPassword()).isEqualTo("pwd");
        assertThat(result.getRoles()).containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");
        verify(userRepository, times(1)).save(toCreate);
    }

    @Test
    void update_whenExists_shouldSetIdAndSave() {
        Long idToUpdate = 20L;
        User incoming = new User();
        incoming.setUsername("bob");
        incoming.setPassword("newpass");
        incoming.setRoles(new HashSet<>(Collections.singleton("ROLE_ADMIN")));
        Set<Calendar> cals = new HashSet<>();
        Calendar cal = new Calendar();
        cal.setId(7L);
        cal.setName("Personal");
        cals.add(cal);
        incoming.setCalendars(cals);

        when(userRepository.existsById(idToUpdate)).thenReturn(true);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        User saved = new User();
        saved.setId(idToUpdate);
        saved.setUsername("bob");
        saved.setPassword("newpass");
        saved.setRoles(new HashSet<>(Collections.singleton("ROLE_ADMIN")));
        saved.setCalendars(cals);
        when(userRepository.save(captor.capture())).thenReturn(saved);

        User result = userService.update(idToUpdate, incoming);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(idToUpdate);
        assertThat(result.getUsername()).isEqualTo("bob");
        assertThat(result.getPassword()).isEqualTo("newpass");
        assertThat(result.getRoles()).containsExactly("ROLE_ADMIN");
        assertThat(result.getCalendars()).hasSize(1)
                .extracting(Calendar::getId)
                .containsExactly(7L);

        User passed = captor.getValue();
        assertThat(passed.getId()).isEqualTo(idToUpdate);
        assertThat(passed.getUsername()).isEqualTo("bob");
        assertThat(passed.getPassword()).isEqualTo("newpass");
        assertThat(passed.getRoles()).containsExactly("ROLE_ADMIN");
        assertThat(passed.getCalendars()).hasSize(1)
                .extracting(Calendar::getId)
                .containsExactly(7L);

        verify(userRepository, times(1)).existsById(idToUpdate);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void update_whenNotExists_shouldReturnNullAndNotSave() {
        Long idToUpdate = 30L;
        User incoming = new User();
        incoming.setUsername("charlie");
        incoming.setPassword("pwd123");
        incoming.setRoles(new HashSet<>(Collections.singleton("ROLE_USER")));

        when(userRepository.existsById(idToUpdate)).thenReturn(false);

        User result = userService.update(idToUpdate, incoming);

        assertThat(result).isNull();
        verify(userRepository, times(1)).existsById(idToUpdate);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void delete_whenExists_shouldCallDeleteById() {
        Long idToDelete = 40L;
        when(userRepository.existsById(idToDelete)).thenReturn(true);

        userService.delete(idToDelete);

        verify(userRepository, times(1)).existsById(idToDelete);
        verify(userRepository, times(1)).deleteById(idToDelete);
    }

    @Test
    void delete_whenNotExists_shouldNotCallDeleteById() {
        Long idToDelete = 50L;
        when(userRepository.existsById(idToDelete)).thenReturn(false);

        userService.delete(idToDelete);

        verify(userRepository, times(1)).existsById(idToDelete);
        verify(userRepository, never()).deleteById(anyLong());
    }
}
