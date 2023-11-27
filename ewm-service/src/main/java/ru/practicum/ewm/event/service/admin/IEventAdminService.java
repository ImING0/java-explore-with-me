package ru.practicum.ewm.event.service.admin;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.dto.EventAdminReqDtoIn;
import ru.practicum.ewm.event.dto.EventFullDtoOut;
import ru.practicum.ewm.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Сервис для работы с событиями в роли администратора
 */
@Service
public interface IEventAdminService {
    /**
     * Метод для обновления события администратором
     *
     * @param eventAdminReqDtoIn данные для обновления события
     * @param eventId            id события
     * @return EventFullDtoOut
     */
    EventFullDtoOut update(EventAdminReqDtoIn eventAdminReqDtoIn,
                           Long eventId);

    /**
     * Метод для получения списка событий по параметрам
     *
     * @param users      список id пользователей
     * @param states     список состояний событий
     * @param categories список id категорий
     * @param rangeStart начало временного диапазона
     * @param rangeEnd   конец временного диапазона
     * @param pageable   параметры пагинации
     * @return список событий
     */
    List<EventFullDtoOut> getAllByParams(Set<Long> users,
                                         Set<EventState> states,
                                         Set<Long> categories,
                                         LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd,
                                         Pageable pageable);
}
