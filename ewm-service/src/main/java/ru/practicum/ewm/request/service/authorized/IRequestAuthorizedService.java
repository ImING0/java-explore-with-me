package ru.practicum.ewm.request.service.authorized;

import ru.practicum.ewm.request.dto.RequestDtoOut;

/**
 * Сервис запросов на участие в мероприятии
 */
public interface IRequestAuthorizedService {
    /**
     * Создание запроса на участие в мероприятии
     *
     * @param userId  id пользователя
     * @param eventId id мероприятия
     * @return DTO запроса на участие в мероприятии исходящий
     */
    RequestDtoOut create(Long userId,
                         Long eventId);
}
