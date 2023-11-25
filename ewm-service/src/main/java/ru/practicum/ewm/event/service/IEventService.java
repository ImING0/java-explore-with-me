package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.EventFullDtoOut;
import ru.practicum.ewm.event.dto.NewEventDtoIn;

/**
 * Сервис для работы с событиями
 */
public interface IEventService {
    EventFullDtoOut create(NewEventDtoIn newEventDtoIn,
                           Long userId);
}
