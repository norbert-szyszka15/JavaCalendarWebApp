package org.example.javacalendarwebapp.calendar;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CalendarService {
    private final CalendarRepository calendarRepository;

    public CalendarService(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    public List<Calendar> findAll() {
        return calendarRepository.findAll();
    }

    public Optional<Calendar> findById(Long id) {
        return calendarRepository.findById(id);
    }

    public Calendar create(Calendar calendar) {
        return calendarRepository.save(calendar);
    }

    public Calendar update(Long id, Calendar calendar) {
        if (!calendarRepository.existsById(id)) {
            return null;
        }
        calendar.setId(id);
        return calendarRepository.save(calendar);
    }

    public void delete(Long id) {
        if (!calendarRepository.existsById(id)) {
            return;
        }
        calendarRepository.deleteById(id);
    }
}
