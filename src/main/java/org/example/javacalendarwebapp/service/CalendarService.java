package org.example.javacalendarwebapp.service;

import org.example.javacalendarwebapp.entity.Calendar;
import org.example.javacalendarwebapp.repository.CalendarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CalendarService {
    private final CalendarRepository calendarRepository;

    public CalendarService(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    public List<Calendar> getAllCalendars() {
        return calendarRepository.findAll();
    }

    public Optional<Calendar> getCalendarById(Long id) {
        return Optional.of(calendarRepository.findById(id).orElse(null));
    }
}
