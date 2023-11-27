package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.user.model.User;

public interface RequestRepository extends JpaRepository<Request, Long> {
    boolean existsByRequesterAndEvent(User requester,
                                      Event event);
}
