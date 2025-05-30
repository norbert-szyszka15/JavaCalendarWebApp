package org.example.javacalendarwebapp.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @ManyToMany(mappedBy = "users")
    private Set<Calendar> calendars = new HashSet<>();
}