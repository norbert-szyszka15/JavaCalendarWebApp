package org.example.javacalendarwebapp.calendar;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Calendar Management", description = "Manage calendars")
@RequestMapping("/calendars")
public class CalendarController {
    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping
    @Operation(summary = "Get all calendars", description = "Retrieve a list of all available calendars.")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<Calendar> getAllCalendars() {
        return calendarService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get calendar by id", description = "Retrieve calendar based on its id.")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Optional<Calendar> getCalendarById(
        @Parameter(description = "ID of the calendar to retrieve", required = true)
        @PathVariable Long id
    ) {
        return Optional.ofNullable(calendarService.findById(id).orElseThrow(() -> new RuntimeException("Event not found with id: " + id)));
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new calendar", description = "Create a new calendar with the provided details.")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Calendar> createCalendar(
        @Parameter(description = "Details of the calendar to create", required = true)
        @RequestBody Calendar calendar
    ) {
        Calendar createdCalendar = calendarService.create(calendar);
        return ResponseEntity.ok(createdCalendar);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing calendar", description = "Update the details of an existing calendar.")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Calendar> updateCalendar(
        @Parameter(description = "ID of the calendar to update", required = true)
        @PathVariable Long id,
        @Parameter(description = "Updated details of the calendar", required = true)
        @RequestBody Calendar calendar
    ) {
        Calendar updatedCalendar = calendarService.update(id, calendar);
        return ResponseEntity.ok(updatedCalendar);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a calendar", description = "Delete a calendar based on its id.")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> deleteCalendar(
        @Parameter(description = "ID of the calendar to delete", required = true)
        @PathVariable Long id
    ) {
        calendarService.delete(id);
        return ResponseEntity.noContent().build();
    }
}