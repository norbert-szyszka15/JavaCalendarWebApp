package org.example.javacalendarwebapp.user;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;
import org.example.javacalendarwebapp.calendar.Calendar;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", nullable = false, columnDefinition = "TEXT")
    private Set<String> roles = new HashSet<>();

    @ManyToMany(mappedBy = "users")
    private Set<Calendar> calendars = new HashSet<>();
}