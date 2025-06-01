package org.example.javacalendarwebapp.event;

import jakarta.persistence.*;
import lombok.Data;
import org.example.javacalendarwebapp.calendar.Calendar;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_title")
    private String title;

    @Column(name = "event_description")
    private String description;

    @Column(name = "event_date")
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "calendar_id",
        foreignKey = @ForeignKey(name = "fk_event_calendar"))
    private Calendar calendar;
}
