package org.example.javacalendarwebapp.repository;

import org.example.javacalendarwebapp.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {    
}