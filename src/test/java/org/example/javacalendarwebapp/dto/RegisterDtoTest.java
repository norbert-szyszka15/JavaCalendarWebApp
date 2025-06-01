package org.example.javacalendarwebapp.dto;

import org.example.javacalendarwebapp.dto.RegisterDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterDtoTest {

    @Test
    void testGettersAndSetters() {
        RegisterDto dto = new RegisterDto();
        dto.setUsername("testuser");
        dto.setPassword("secret");

        assertEquals("testuser", dto.getUsername());
        assertEquals("secret", dto.getPassword());
    }
}
