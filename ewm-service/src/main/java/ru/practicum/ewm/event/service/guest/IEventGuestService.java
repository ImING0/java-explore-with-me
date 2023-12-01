package ru.practicum.ewm.event.service.guest;

import ru.practicum.ewm.dto.StatsRequestDto;
import ru.practicum.ewm.event.dto.event.EventFullDtoOut;
import ru.practicum.ewm.event.dto.event.EventShortDtoOut;
import ru.practicum.ewm.event.util.SortByForEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Сервис для получения информации о мероприятиях для всех пользователей
 */
public interface IEventGuestService {
    /**
     * Получение полной информации о мероприятии
     *
     * @param id              идентификатор мероприятия
     * @param statsRequestDto dto для получения статистики
     * @return полная информация о мероприятии
     */
    EventFullDtoOut getById(Long id,
                            StatsRequestDto statsRequestDto);

    /**
     * Получение списка мероприятий по параметрам
     *
     * @param text            текст для поиска
     * @param categories      категории
     * @param paid            платное/бесплатное
     * @param rangeStart      дата начала
     * @param rangeEnd        дата окончания
     * @param onlyAvailable   только доступные
     * @param sort            сортировка
     * @param from            от
     * @param size            размер
     * @param statsRequestDto dto для получения статистики
     * @return список мероприятий
     */
    List<EventShortDtoOut> getAllByParams(String text,
                                          Set<Long> categories,
                                          Boolean paid,
                                          LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd,
                                          Boolean onlyAvailable,
                                          SortByForEvent sort,
                                          Integer from,
                                          Integer size,
                                          StatsRequestDto statsRequestDto);
}
