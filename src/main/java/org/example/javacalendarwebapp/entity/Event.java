package org.example.javacalendarwebapp.entity;

import org.example.javacalendarwebapp.config.ApplicationContextProvider;
import org.example.javacalendarwebapp.priority.HighPriority;
import org.example.javacalendarwebapp.priority.LowPriority;
import org.example.javacalendarwebapp.priority.MediumPriority;
import org.example.javacalendarwebapp.priority.PriorityLevel;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_title", nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(name = "event_description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_priority", nullable = true)
    private TaskPriority priority;

    @Transient
    private PriorityLevel priorityLevel;

    @PostLoad
    @PostPersist
    private void initPriorityLevel() {
        switch (priority) {
            case HIGH: {
                priorityLevel = ApplicationContextProvider.getBean(HighPriority.class);
                break;
            }
            case MEDIUM: {
                priorityLevel = ApplicationContextProvider.getBean(MediumPriority.class);
                break;
            }
            case LOW:
            default: {
                priorityLevel = ApplicationContextProvider.getBean(LowPriority.class);
                break;
            }
            
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "calendar_id",
        foreignKey = @ForeignKey(name = "fk_event_calendar"),
        nullable = false)
    private Calendar calendar;
}
