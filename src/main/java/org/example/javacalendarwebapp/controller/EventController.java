package org.example.javacalendarwebapp.controller;

import java.util.List;

import org.example.javacalendarwebapp.entity.Event;
import org.example.javacalendarwebapp.service.EventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Event Management", description = "Manage calendar events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    @Operation(summary = "Get all events", description = "Retrieve a list of all calendar events.")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }
}