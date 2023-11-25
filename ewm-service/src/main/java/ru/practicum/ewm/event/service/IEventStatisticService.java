package ru.practicum.ewm.event.service;

/**
 * Интерфейс сервиса для работы со статистикой событий
 */
public interface IEventStatisticService {
    /**
     * Получение количества просмотров события по его идентификатору
     *
     * @param eventId идентификатор события
     * @return количество просмотров события
     */
    Long getViewsByEventId(Long eventId);
}
