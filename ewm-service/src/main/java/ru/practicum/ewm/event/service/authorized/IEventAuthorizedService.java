package ru.practicum.ewm.event.service.authorized;

import ru.practicum.ewm.event.dto.EventFullDtoOut;
import ru.practicum.ewm.event.dto.NewEventDtoIn;

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
}
