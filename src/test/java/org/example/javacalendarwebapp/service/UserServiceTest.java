package org.example.javacalendarwebapp.service;

import org.example.javacalendarwebapp.entity.User;
import org.example.javacalendarwebapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ReturnsListOfUsers() {
        User u1 = new User(); u1.setId(1L); u1.setUsername("u1");
        User u2 = new User(); u2.setId(2L); u2.setUsername("u2");
        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        List<User> result = userService.findAll();
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void findById_UserExists_ReturnsOptional() {
        User u = new User(); u.setId(5L); u.setUsername("user");
        when(userRepository.findById(5L)).thenReturn(Optional.of(u));

        Optional<User> result = userService.findById(5L);
        assertTrue(result.isPresent());
        assertEquals("user", result.get().getUsername());
        verify(userRepository, times(1)).findById(5L);
    }

    @Test
    void findById_UserNotExists_ReturnsEmpty() {
        when(userRepository.findById(10L)).thenReturn(Optional.empty());
        Optional<User> result = userService.findById(10L);
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(10L);
    }

    @Test
    void create_SavesAndReturnsUser() {
        User toSave = new User(); toSave.setUsername("newuser");
        User saved = new User(); saved.setId(7L); saved.setUsername("newuser");
        when(userRepository.save(toSave)).thenReturn(saved);

        User result = userService.create(toSave);
        assertEquals(7L, result.getId());
        assertEquals("newuser", result.getUsername());
        verify(userRepository, times(1)).save(toSave);
    }

    @Test
    void update_UserExists_ReturnsUpdated() {
        User updated = new User(); updated.setUsername("upd");
        when(userRepository.existsById(3L)).thenReturn(true);
        // Kiedy wywołamy save, zwróćmy obiekt z id
        User saved = new User(); saved.setId(3L); saved.setUsername("upd");
        when(userRepository.save(any(User.class))).thenReturn(saved);

        User result = userService.update(3L, updated);
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("upd", result.getUsername());
        verify(userRepository, times(1)).existsById(3L);
        verify(userRepository, times(1)).save(updated);
    }

    @Test
    void update_UserNotExists_ReturnsNull() {
        when(userRepository.existsById(4L)).thenReturn(false);
        User toUpdate = new User(); toUpdate.setUsername("x");
        User result = userService.update(4L, toUpdate);
        assertNull(result);
        verify(userRepository, times(1)).existsById(4L);
        verify(userRepository, never()).save(any());
    }

    @Test
    void delete_UserExists_Deletes() {
        when(userRepository.existsById(8L)).thenReturn(true);
        userService.delete(8L);
        verify(userRepository, times(1)).existsById(8L);
        verify(userRepository, times(1)).deleteById(8L);
    }

    @Test
    void delete_UserNotExists_DoesNothing() {
        when(userRepository.existsById(9L)).thenReturn(false);
        userService.delete(9L);
        verify(userRepository, times(1)).existsById(9L);
        verify(userRepository, never()).deleteById(anyLong());
    }
}
