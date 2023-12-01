package ru.practicum.ewm.event.service.guest;

import ru.practicum.ewm.dto.StatsRequestDto;
import ru.practicum.ewm.event.Util.SortByForEvent;
import ru.practicum.ewm.event.dto.event.EventFullDtoOut;
import ru.practicum.ewm.event.dto.event.EventShortDtoOut;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface IEventGuestService {
    EventFullDtoOut getById(Long id,
                            StatsRequestDto statsRequestDto);

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
