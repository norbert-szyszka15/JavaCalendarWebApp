package org.example.javacalendarwebapp.entity;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {

    @Test
    void testUserGettersAndSetters() {
        User user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setPassword("pass");

        Set<String> roles = new HashSet<>();
        roles.add("USER");
        user.setRoles(roles);

        assertEquals(1L, user.getId());
        assertEquals("john", user.getUsername());
        assertEquals("pass", user.getPassword());
        assertTrue(user.getRoles().contains("USER"));

        Calendar cal = new Calendar();
        cal.setId(2L);
        cal.setName("TestCal");
        user.getCalendars().add(cal);

        assertTrue(user.getCalendars().contains(cal));
    }
}
