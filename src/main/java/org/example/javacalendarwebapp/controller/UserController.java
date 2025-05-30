package org.example.javacalendarwebapp.controller;

import java.util.List;
import java.util.Optional;

import org.example.javacalendarwebapp.entity.User;
import org.example.javacalendarwebapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "User Management", description = "Manage user accounts and profiles")
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping
    @Operation(summary = "Get user profile", description = "Retrieve the profile information of the currently authenticated user.")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id", description = "Retrieve a specific user profile based on its id.")
    public Optional<User> getUserById(
        @Parameter(description = "ID of the user to retrieve", required = true)
        @PathVariable Long id
    ) {
        return Optional.ofNullable(userService.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id)));
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new user", description = "Create a new user profile with the provided details.")
    public ResponseEntity<User> createUser(
        @Parameter(description = "Details of the user to create", required = true)
        User user
    ) {
        User createdUser = userService.create(user);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing user", description = "Update the details of an existing user profile.")
    public ResponseEntity<User> updateUser(
        @Parameter(description = "ID of the user to update", required = true)
        @PathVariable Long id,
        @Parameter(description = "Updated details of the user", required = true)
        User user
    ) {
        User updatedUser = userService.update(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Delete a user profile based on its id.")
    public ResponseEntity<Void> deleteUser(
        @Parameter(description = "ID of the user to delete", required = true)
        @PathVariable Long id
    ) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}