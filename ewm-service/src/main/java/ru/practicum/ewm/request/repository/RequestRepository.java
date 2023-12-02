package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.user.model.User;

import java.util.List;
import java.util.Set;

public interface RequestRepository extends JpaRepository<Request, Long> {
    /**
     * Проверка существования запроса на участие в мероприятии
     *
     * @param requester инициатор запроса
     * @param event     мероприятие
     * @return true, если запрос существует, иначе false
     */
    boolean existsByRequesterAndEvent(User requester,
                                      Event event);

    /**
     * Поиск всех запросов на участие в мероприятии для пользователя
     *
     * @param user пользователь
     * @return список всех запросов на участие в мероприятии для пользователя
     */
    List<Request> findAllByRequester(User user);

    /**
     * Получение информации о запросах на участие в событии текущего пользователя
     *
     * @param event     событие
     * @param initiator инициатор события
     * @return список запросов на участие в событии текущего пользователя
     */
    List<Request> findAllByEventAndEventInitiator(Event event,
                                                  User initiator);

    /**
     * Получить все запросы на участие по их id
     *
     * @param requestsId id запросов
     * @return список запросов
     */
    List<Request> findAllByIdIn(Set<Long> requestsId);
}
