package ru.practicum.ewm.event.service.internal.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.StatsRequestDto;
import ru.practicum.ewm.dto.StatsResponseDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.service.internal.IEventStatisticService;
import ru.practicum.ewm.statistic.IStatisticService;
import ru.practicum.ewm.statistic.constant.StatsConstants;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EventStatisticService implements IEventStatisticService {
    private final IStatisticService statisticService;

    @Override
    public Long getViewsByEvent(Event event) {
        Long views = 0L;
        List<StatsResponseDto> statsResponseDtoList = statisticService.getStats(
                event.getCreatedOn(), LocalDateTime.now()
                        .plusSeconds(3L),
                Set.of(StatsConstants.EVENT_BASE_GUEST_PATH + "/" + event.getId()), true);
        StatsResponseDto statResponseDto;
        if (!statsResponseDtoList.isEmpty()) {
            statResponseDto = statsResponseDtoList.get(0);
            views = statResponseDto.getHits();
        }
        return views;
    }

    @Override
    public void addHit(StatsRequestDto statsRequestDto) {
        statisticService.addStat(statsRequestDto);
    }
}
