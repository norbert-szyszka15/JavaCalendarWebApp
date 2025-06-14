package org.example.javacalendarwebapp.event;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Tag(name = "Event Management", description = "Manage calendar events")
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    @Operation(summary = "Get all events", description = "Retrieve a list of all calendar events.")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<Event> getAllEvents() {
        return eventService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get event by id", description = "Retrieve a calendar event based on its id.")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Event> getEventById(
        @Parameter(description = "ID of the event to retrieve", required = true)
        @PathVariable Long id
    ) {
        Event found = eventService.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        return ResponseEntity.ok(found);    }

    @PostMapping("/create")
    @Operation(summary = "Create a new event", description = "Create a new calendar event with the provided details.")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Event> createEvent(
        @Parameter(description = "Details of the event to create", required = true)
        @RequestBody Event event
    ) {
        Event createdEvent = eventService.create(event);
        return ResponseEntity.ok(createdEvent);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing event", description = "Update the details of an existing calendar event.")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Event> updateEvent(
        @Parameter(description = "ID of the event to update", required = true)
        @PathVariable Long id,
        @Parameter(description = "Updated details of the event", required = true)
        @RequestBody Event event
    ) {
        Event updatedEvent = eventService.update(id, event);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an event", description = "Delete a calendar event based on its id.")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> deleteEvent(
        @Parameter(description = "ID of the event to delete", required = true)
        @PathVariable Long id
    ) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/date")
    @Operation(summary = "Get event date", description = "Returns only the event's date for given ID")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<LocalDateTime> getEventDateById(
            @Parameter(description = "ID of the event", required = true)
            @PathVariable Long id
    ) {
        try {
            LocalDateTime date = eventService.getEventDateByID(id);
            if (date == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(date);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}