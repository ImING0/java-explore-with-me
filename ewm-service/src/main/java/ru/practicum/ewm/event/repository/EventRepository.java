package ru.practicum.ewm.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Найти все события по идентификатору пользователя, состоянию, категории и дате события
     *
     * @param users      идентификаторы пользователей
     * @param states     состояния событий
     * @param categories идентификаторы категорий
     * @param rangeStart начало диапазона даты события
     * @param rangeEnd   конец диапазона даты события
     * @param pageable   параметры пагинации
     * @return список событий
     */
    List<Event> findAllByInitiator_IdInAndStateInAndCategory_IdInAndEventDateBetween(Set<Long> users,
                                                                                     Set<EventState> states,
                                                                                     Set<Long> categories,
                                                                                     LocalDateTime rangeStart,
                                                                                     LocalDateTime rangeEnd,
                                                                                     Pageable pageable);

    /**
     * Найти все события созданные пользователем
     *
     * @param initiator пользователь создавший событие
     * @param pageable  параметры пагинации
     * @return список событий
     */
    List<Event> findAllByInitiator(User initiator,
                                   Pageable pageable);

    /**
     * Найти событие по идентификатору и пользователю создавшему событие
     *
     * @param eventId   идентификатор события
     * @param initiator пользователь создавший событие
     * @return событие
     */
    Optional<Event> findByIdAndInitiator(Long eventId,
                                         User initiator);
}
