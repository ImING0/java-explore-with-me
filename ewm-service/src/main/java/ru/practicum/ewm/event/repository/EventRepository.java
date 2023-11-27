package ru.practicum.ewm.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByInitiator_IdInAndStateInAndCategory_IdInAndEventDateBetween(Set<Long> users,
                                                                                     Set<EventState> states,
                                                                                     Set<Long> categories,
                                                                                     LocalDateTime rangeStart,
                                                                                     LocalDateTime rangeEnd,
                                                                                     Pageable pageable);
}
