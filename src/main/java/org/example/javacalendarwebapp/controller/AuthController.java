package org.example.javacalendarwebapp.controller;

import java.net.URI;

import org.example.javacalendarwebapp.dto.RegisterDto;
import org.example.javacalendarwebapp.entity.User;
import org.example.javacalendarwebapp.service.JpaUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JpaUserDetailsService uds;

    public AuthController(JpaUserDetailsService uds) {
        this.uds = uds;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterDto dto) {
        User created = uds.registerNew(dto.getUsername(), dto.getPassword());
        return ResponseEntity.created(
                URI.create("/auth/register/" + created.getId())).build();
    }
}
