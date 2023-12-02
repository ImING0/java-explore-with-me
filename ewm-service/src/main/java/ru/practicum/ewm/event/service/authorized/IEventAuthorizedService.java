package ru.practicum.ewm.event.service.authorized;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.event.dto.event.EventFullDtoOut;
import ru.practicum.ewm.event.dto.event.EventShortDtoOut;
import ru.practicum.ewm.event.dto.event.EventUserUpdDtoIn;
import ru.practicum.ewm.event.dto.event.NewEventDtoIn;
import ru.practicum.ewm.event.dto.request.EventRequestStatusUpdDtoIn;
import ru.practicum.ewm.event.dto.request.EventRequestStatusUpdDtoOut;
import ru.practicum.ewm.request.dto.RequestDtoOut;

import java.util.List;

/**
 * Сервис для работы с событиями только для авторизованных пользователей
 */
public interface IEventAuthorizedService {
    /**
     * Создание события
     *
     * @param newEventDtoIn данные для создания события
     * @param userId        идентификатор пользователя
     * @return созданное событие
     */
    EventFullDtoOut create(NewEventDtoIn newEventDtoIn,
                           Long userId);

    /**
     * Получение всех событий добавленных текущим пользователем
     *
     * @param userId   идентификатор пользователя
     * @param pageable параметры пагинации
     * @return список событий
     */
    List<EventShortDtoOut> getAllForCurrentUser(Long userId,
                                                Pageable pageable);

    /**
     * Получение полной информации о событии добавленном текущим пользователем
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return полная информация о событии
     */
    EventFullDtoOut getByIdForCurrentUser(Long userId,
                                          Long eventId);

    /**
     * Обновление события добавленного текущим пользователем
     *
     * @param eventUserUpdDtoIn данные для обновления события
     * @param userId            идентификатор пользователя
     * @param eventId           идентификатор события
     * @return обновленное событие
     */
    EventFullDtoOut updateForCurrentUser(EventUserUpdDtoIn eventUserUpdDtoIn,
                                         Long userId,
                                         Long eventId);

    /**
     * Получение информации о запросах на участие в событии текущего пользователя
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return список заявок на участие в событии созданным текущим пользователем
     */

    List<RequestDtoOut> getAllRequestByEventIdForCurrentUser(Long userId,
                                                             Long eventId);

    /**
     * Обновление статуса заявок на участие в событии текущего пользователя
     *
     * @param eventRequestStatusUpdDtoIn данные для обновления статуса заявок
     * @param userId                     идентификатор пользователя
     * @param eventId                    идентификатор события
     * @return обновленное событие
     */
    EventRequestStatusUpdDtoOut updateRequestsStatusForCurrentUser(EventRequestStatusUpdDtoIn eventRequestStatusUpdDtoIn,
                                                                   Long userId,
                                                                   Long eventId);
}
