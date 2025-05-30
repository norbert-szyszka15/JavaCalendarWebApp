package org.example.javacalendarwebapp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_title", nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(name = "task_description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "calendar_id",
        foreignKey = @ForeignKey(name = "fk_task_calendar"),
        nullable = false)
    private Calendar calendar;
}
