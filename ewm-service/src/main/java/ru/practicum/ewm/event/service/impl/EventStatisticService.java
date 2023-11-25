package ru.practicum.ewm.event.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.service.IEventStatisticService;

@Service
@RequiredArgsConstructor
public class EventStatisticService implements IEventStatisticService {
    @Override
    public Long getViewsByEventId(Long eventId) {
        return null;
    }
}
