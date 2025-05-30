package org.example.javacalendarwebapp.controller;

import org.example.javacalendarwebapp.entity.Calendar;
import org.example.javacalendarwebapp.service.CalendarService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@Tag(name = "Calendar Management", description = "Manage calendars")
public class CalendarController {
    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping
    @Operation(summary = "Get all calendars", description = "Retrieve a list of all available calendars.")
    public List<Calendar> getAllCalendars() {
        return calendarService.getAllCalendars();
    }
}