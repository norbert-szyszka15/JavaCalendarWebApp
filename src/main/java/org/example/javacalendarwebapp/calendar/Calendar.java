package org.example.javacalendarwebapp.calendar;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;
import org.example.javacalendarwebapp.event.Event;
import org.example.javacalendarwebapp.task.Task;
import org.example.javacalendarwebapp.user.User;

@Entity
@Data
@Table(name = "calendars")
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "calendar_name")
    private String name;

    @ManyToMany
    @JoinTable(
        name = "calendars_users",
        joinColumns =  @JoinColumn(name = "calendar_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();
    
    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> tasks = new HashSet<>();

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Event> events = new HashSet<>();
}