package org.example.javacalendarwebapp.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

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
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id", description = "Retrieve a specific user profile based on its id.")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<User> getUserById(
        @Parameter(description = "ID of the user to retrieve", required = true)
        @PathVariable Long id
    ) {
        User found = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return ResponseEntity.ok(found);
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new user", description = "Create a new user profile with the provided details.")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<User> createUser(
        @Parameter(description = "Details of the user to create", required = true)
        @RequestBody User user
    ) {
        User createdUser = userService.create(user);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing user", description = "Update the details of an existing user profile.")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<User> updateUser(
        @Parameter(description = "ID of the user to update", required = true)
        @PathVariable Long id,
        @Parameter(description = "Updated details of the user", required = true)
        @RequestBody User user
    ) {
        User updatedUser = userService.update(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Delete a user profile based on its id.")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(
        @Parameter(description = "ID of the user to delete", required = true)
        @PathVariable Long id
    ) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}