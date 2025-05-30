package org.example.javacalendarwebapp.controller;

import java.util.List;

import org.example.javacalendarwebapp.entity.User;
import org.example.javacalendarwebapp.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "User Management", description = "Manage user accounts and profiles")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping
    @Operation(summary = "Get user profile", description = "Retrieve the profile information of the currently authenticated user.")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    
}