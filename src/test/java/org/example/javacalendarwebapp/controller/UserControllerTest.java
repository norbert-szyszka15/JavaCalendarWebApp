// src/test/java/org/example/javacalendarwebapp/controller/UserControllerTest.java
package org.example.javacalendarwebapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.javacalendarwebapp.entity.User;
import org.example.javacalendarwebapp.service.UserService;
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

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllUsers_ReturnsList() throws Exception {
        User u1 = new User(); u1.setId(1L); u1.setUsername("u1");
        User u2 = new User(); u2.setId(2L); u2.setUsername("u2");
        when(userService.findAll()).thenReturn(List.of(u1, u2));

        mockMvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(u1, u2))));
        verify(userService, times(1)).findAll();
    }

    @Test
    void getUser_Exists_ReturnsUser() throws Exception {
        User u = new User(); u.setId(5L); u.setUsername("john");
        when(userService.findById(5L)).thenReturn(Optional.of(u));

        mockMvc.perform(get("/users/5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(u)));
        verify(userService, times(1)).findById(5L);
    }

    @Test
    void getUser_NotExists_Returns500() throws Exception {
        when(userService.findById(10L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
        verify(userService, times(1)).findById(10L);
    }

    @Test
    void createUser_Valid_ReturnsCreatedUser() throws Exception {
        User input = new User(); input.setUsername("new");
        User saved = new User(); saved.setId(7L); saved.setUsername("new");
        when(userService.create(any(User.class))).thenReturn(saved);

        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(saved)));
        verify(userService, times(1)).create(any(User.class));
    }

    @Test
    void updateUser_Exists_ReturnsUpdated() throws Exception {
        User upd = new User(); upd.setUsername("upd");
        User saved = new User(); saved.setId(3L); saved.setUsername("upd");
        when(userService.update(eq(3L), any(User.class))).thenReturn(saved);

        mockMvc.perform(put("/users/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(upd)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(saved)));
        verify(userService, times(1)).update(eq(3L), any(User.class));
    }

    @Test
    void updateUser_NotExists_Returns200WithNullBody() throws Exception {
        when(userService.update(eq(4L), any(User.class))).thenReturn(null);

        mockMvc.perform(put("/users/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User())))
                .andExpect(status().isOk())
                .andExpect(content().string("")); // puste ciało
        verify(userService, times(1)).update(eq(4L), any(User.class));
    }

    @Test
    void deleteUser_Exists_ReturnsNoContent() throws Exception {
        doNothing().when(userService).delete(8L);

        mockMvc.perform(delete("/users/8"))
                .andExpect(status().isNoContent());
        verify(userService, times(1)).delete(8L);
    }
}
