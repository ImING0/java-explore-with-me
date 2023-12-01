package ru.practicum.ewm.event.service.internal;

import ru.practicum.ewm.dto.StatsRequestDto;
import ru.practicum.ewm.event.model.Event;

/**
 * Интерфейс сервиса для работы со статистикой событий
 * Реализация этого интерфейса должна использовать общий интерфейс для работы с сервером статистики
 * ru.practicum.ewm.statistic.impl.StatisticService
 */
public interface IEventStatisticService {
    /**
     * Получение количества просмотров события по его идентификатору(см. реализацию)
     *
     * @param event идентификатор события
     * @return количество просмотров события
     */
    Long getViewsByEvent(Event event);

    /**
     * Добавление статистики просмотров события
     *
     * @param statsRequestDto объект запроса
     */
    void addHit(StatsRequestDto statsRequestDto);
}
