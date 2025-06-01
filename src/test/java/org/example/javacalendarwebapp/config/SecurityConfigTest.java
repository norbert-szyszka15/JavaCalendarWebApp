package org.example.javacalendarwebapp.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {

    @Test
    void passwordEncoderIsBCrypt() {
        SecurityConfig config = new SecurityConfig();
        PasswordEncoder encoder = config.passwordEncoder();
        assertInstanceOf(BCryptPasswordEncoder.class, encoder, "passwordEncoder powinien być instancją BCryptPasswordEncoder");
    }
}
