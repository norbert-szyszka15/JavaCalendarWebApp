package org.example.javacalendarwebapp.repository;

import org.example.javacalendarwebapp.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
}
