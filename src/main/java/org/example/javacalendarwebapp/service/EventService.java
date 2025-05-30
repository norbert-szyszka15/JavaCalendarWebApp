package org.example.javacalendarwebapp.service;

import org.example.javacalendarwebapp.entity.Event;
import org.example.javacalendarwebapp.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(Long id) {
        return Optional.of(eventRepository.findById(id).orElse(null));
    }
}
