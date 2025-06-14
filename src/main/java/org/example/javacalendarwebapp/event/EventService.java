package org.example.javacalendarwebapp.event;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        return eventRepository.findById(id);
    }

    public Event create(Event event) {
        return eventRepository.save(event);
    }

    public Event update(Long id, Event event) {
        if (!eventRepository.existsById(id)) {
            return null;
        }
        event.setId(id);
        return eventRepository.save(event);
    }

    public void delete(Long id) {
        if (!eventRepository.existsById(id)) {
            return;
        }
        eventRepository.deleteById(id);
    }

    public LocalDateTime getEventDateByID(Long id) {
        Event e = eventRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Event not found with id: " + id));
        return e.getDate();
    }
}
