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

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Optional<Event> findById(Long id) {
        return Optional.of(eventRepository.findById(id).orElse(null));
    }

    public Event create(Event event) {
        return eventRepository.save(event);
    }

    public Event update(Long id, Event event) {
        if (!eventRepository.existsById(id)) {
            return null; // or throw an exception
        }
        event.setId(id);
        return eventRepository.save(event);
    }

    public void delete(Long id) {
        if (!eventRepository.existsById(id)) {
            return; // or throw an exception
        }
        eventRepository.deleteById(id);
    }
}
