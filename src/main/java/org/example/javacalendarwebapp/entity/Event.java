package org.example.javacalendarwebapp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

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

    @Column(name = "event_date", nullable = false)
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "calendar_id",
        foreignKey = @ForeignKey(name = "fk_event_calendar"),
        nullable = false)
    private Calendar calendar;
}
