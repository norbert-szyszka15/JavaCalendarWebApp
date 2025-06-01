package org.example.javacalendarwebapp.user;

import org.example.javacalendarwebapp.calendar.Calendar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void whenNewUser_thenFieldsAreDefault() {
        // Nowa instancja User powinna mieć id null
        assertNull(user.getId(), "Id powinno być null w nowym User");
        // username i password null
        assertNull(user.getUsername(), "Username powinno być null w nowym User");
        assertNull(user.getPassword(), "Password powinno być null w nowym User");
        // roles powinno być zainicjalizowane, ale puste
        assertNotNull(user.getRoles(), "Roles powinno być zainicjalizowane (nie null) w nowym User");
        assertTrue(user.getRoles().isEmpty(), "Roles powinno być puste w nowym User");
        // calendars powinno być zainicjalizowane, ale puste
        assertNotNull(user.getCalendars(), "Calendars powinno być zainicjalizowane (nie null) w nowym User");
        assertTrue(user.getCalendars().isEmpty(), "Calendars powinno być puste w nowym User");
    }

    @Test
    void whenSetAndGetId_thenReturnCorrectValue() {
        user.setId(42L);
        assertEquals(42L, user.getId(), "Getter powinien zwrócić id, które zostało ustawione");
    }

    @Test
    void whenSetAndGetUsername_thenReturnCorrectValue() {
        String username = "john_doe";
        user.setUsername(username);
        assertEquals(username, user.getUsername(), "Getter powinien zwrócić username, które zostało ustawione");
    }

    @Test
    void whenSetAndGetPassword_thenReturnCorrectValue() {
        String password = "securePass123";
        user.setPassword(password);
        assertEquals(password, user.getPassword(), "Getter powinien zwrócić password, które zostało ustawione");
    }

    @Test
    void whenSetAndGetRoles_thenReturnCorrectValue() {
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        roles.add("ROLE_ADMIN");
        user.setRoles(roles);
        assertNotNull(user.getRoles(), "Roles nie powinno być null po ustawieniu");
        assertEquals(2, user.getRoles().size(), "Roles powinno zawierać 2 elementy");
        assertTrue(user.getRoles().contains("ROLE_USER"), "Roles powinno zawierać „ROLE_USER”");
        assertTrue(user.getRoles().contains("ROLE_ADMIN"), "Roles powinno zawierać „ROLE_ADMIN”");
    }

    @Test
    void whenAddRole_thenRolesUpdated() {
        user.getRoles().add("ROLE_USER");
        assertEquals(1, user.getRoles().size(), "Roles powinno zawierać 1 element po dodaniu");
        assertTrue(user.getRoles().contains("ROLE_USER"), "Roles powinno zawierać „ROLE_USER”");
        user.getRoles().add("ROLE_ADMIN");
        assertEquals(2, user.getRoles().size(), "Roles powinno zawierać 2 elementy po dodaniu drugiej roli");
        assertTrue(user.getRoles().contains("ROLE_ADMIN"), "Roles powinno zawierać „ROLE_ADMIN”");
    }

    @Test
    void whenSetAndGetCalendars_thenReturnCorrectValue() {
        Calendar cal1 = new Calendar();
        cal1.setId(10L);
        cal1.setName("Work Calendar");
        Calendar cal2 = new Calendar();
        cal2.setId(20L);
        cal2.setName("Personal Calendar");

        Set<Calendar> calendars = new HashSet<>();
        calendars.add(cal1);
        calendars.add(cal2);

        user.setCalendars(calendars);
        assertNotNull(user.getCalendars(), "Calendars nie powinno być null po ustawieniu");
        assertEquals(2, user.getCalendars().size(), "Calendars powinno zawierać 2 elementy");
        assertTrue(user.getCalendars().stream().anyMatch(c -> c.getId().equals(10L)), "Calendars powinno zawierać kalendarz o id 10");
        assertTrue(user.getCalendars().stream().anyMatch(c -> c.getId().equals(20L)), "Calendars powinno zawierać kalendarz o id 20");
    }

    @Test
    void whenAddCalendar_thenCalendarsUpdated() {
        Calendar cal = new Calendar();
        cal.setId(5L);
        cal.setName("Test Calendar");
        user.getCalendars().add(cal);

        assertEquals(1, user.getCalendars().size(), "Calendars powinno zawierać 1 element po dodaniu");
        assertTrue(user.getCalendars().stream().anyMatch(c -> c.getId().equals(5L)), "Calendars powinno zawierać kalendarz o id 5");
    }
}
