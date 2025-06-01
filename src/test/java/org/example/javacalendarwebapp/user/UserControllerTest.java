package org.example.javacalendarwebapp.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.javacalendarwebapp.calendar.Calendar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        objectMapper = new ObjectMapper();
        MappingJackson2HttpMessageConverter jacksonConverter =
                new MappingJackson2HttpMessageConverter(objectMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
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
    void getAllUsers_shouldReturnListOfUsers() throws Exception {
        User u1 = new User();
        u1.setId(1L);
        u1.setUsername("alice");
        User u2 = new User();
        u2.setId(2L);
        u2.setUsername("bob");
        List<User> users = Arrays.asList(u1, u2);

        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("alice"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].username").value("bob"));

        verify(userService, times(1)).findAll();
    }

    @Test
    void getUserById_whenExists_shouldReturnUser() throws Exception {
        User u = new User();
        u.setId(10L);
        u.setUsername("charlie");
        u.setPassword("secret");
        Set<String> roles = new HashSet<>(List.of("ROLE_USER"));
        u.setRoles(roles);
        Calendar cal = new Calendar();
        cal.setId(5L);
        cal.setName("Work");
        Set<Calendar> cals = new HashSet<>(Collections.singletonList(cal));
        u.setCalendars(cals);

        when(userService.findById(10L)).thenReturn(Optional.of(u));

        mockMvc.perform(get("/users/{id}", 10L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.username").value("charlie"))
                .andExpect(jsonPath("$.password").value("secret"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"))
                .andExpect(jsonPath("$.calendars[0].id").value(5))
                .andExpect(jsonPath("$.calendars[0].name").value("Work"));

        verify(userService, times(1)).findById(10L);
    }

    @Test
    void getUserById_whenNotFound_shouldReturn404() throws Exception {
        when(userService.findById(5L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("\"User not found with id: 5\""));

        verify(userService, times(1)).findById(5L);
    }

    @Test
    void createUser_shouldReturnCreatedUser() throws Exception {
        User input = new User();
        input.setUsername("dave");
        input.setPassword("pwd123");
        Set<String> roles = new HashSet<>(Arrays.asList("ROLE_USER", "ROLE_ADMIN"));
        input.setRoles(roles);

        User saved = new User();
        saved.setId(100L);
        saved.setUsername("dave");
        saved.setPassword("pwd123");
        saved.setRoles(roles);

        when(userService.create(any(User.class))).thenReturn(saved);

        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.username").value("dave"))
                .andExpect(jsonPath("$.password").value("pwd123"))
                .andExpect(jsonPath("$.roles.length()").value(2));

        verify(userService, times(1)).create(any(User.class));
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() throws Exception {
        Long idToUpdate = 50L;
        User input = new User();
        input.setUsername("eve");
        input.setPassword("newpass");
        Set<String> roles = new HashSet<>(Collections.singletonList("ROLE_USER"));
        input.setRoles(roles);

        User updated = new User();
        updated.setId(idToUpdate);
        updated.setUsername("eve");
        updated.setPassword("newpass");
        updated.setRoles(roles);

        when(userService.update(eq(idToUpdate), any(User.class))).thenReturn(updated);

        mockMvc.perform(put("/users/{id}", idToUpdate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(50))
                .andExpect(jsonPath("$.username").value("eve"))
                .andExpect(jsonPath("$.password").value("newpass"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));

        verify(userService, times(1)).update(eq(idToUpdate), any(User.class));
    }

    @Test
    void deleteUser_shouldReturnNoContent() throws Exception {
        doNothing().when(userService).delete(77L);

        mockMvc.perform(delete("/users/{id}", 77L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).delete(77L);
    }
}
