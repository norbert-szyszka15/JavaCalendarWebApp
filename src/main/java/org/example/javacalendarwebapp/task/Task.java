package org.example.javacalendarwebapp.task;

import jakarta.persistence.*;
import lombok.Data;
import org.example.javacalendarwebapp.calendar.Calendar;
import org.example.javacalendarwebapp.task.priority.TaskPriorityType;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_title")
    private String title;

    @Column(name = "task_description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_priority")
    private TaskPriorityType priority;

    @Column(name = "task_date")
    private LocalDateTime date;

    @Column(name = "completed")
    private Boolean completed = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "calendar_id",
        foreignKey = @ForeignKey(name = "fk_task_calendar"))
    private Calendar calendar;
}
