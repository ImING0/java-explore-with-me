package ru.practicum.ewm.service.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.service.event.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
