package ru.practicum.ewm.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long>,
        QuerydslPredicateExecutor<Event> {

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

   /* @Query("SELECT e FROM Event e WHERE "
            + "(LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%')) "
            + "OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%'))) "
            + "AND ((:categories) is null or e.category.id IN :categories)"
            + "AND (:paid IS NULL OR e.paid = :paid)"
            + "AND (e.eventDate > CURRENT_TIMESTAMP OR e.eventDate BETWEEN COALESCE(:rangeStart,"
            + " CURRENT_TIMESTAMP) AND COALESCE(:rangeEnd, '2099-12-31')) "
            + "AND ((:onlyAvailable = false ) or (e.confirmedRequests < e.participantLimit))"
            + "AND e.state = 'PUBLISHED' ")
    List<Event> findEvents(@Param("text") String text,
                           @Param("categories") Set<Long> categories,
                           @Param("paid") boolean paid,
                           @Param("rangeStart") LocalDateTime rangeStart,
                           @Param("rangeEnd") LocalDateTime rangeEnd,
                           @Param("onlyAvailable") Boolean onlyAvailable,
                           Pageable pageable);*/
}