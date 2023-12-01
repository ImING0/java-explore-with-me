package ru.practicum.ewm.event.service.internal;

import ru.practicum.ewm.dto.StatsRequestDto;
import ru.practicum.ewm.event.model.Event;

/**
 * Интерфейс сервиса для работы со статистикой событий
 */
public interface IEventStatisticService {
    /**
     * Получение количества просмотров события по его идентификатору
     *
     * @param event идентификатор события
     * @return количество просмотров события
     */
    Long getViewsByEvent(Event event);

    void addHit(StatsRequestDto statsRequestDto);
}
