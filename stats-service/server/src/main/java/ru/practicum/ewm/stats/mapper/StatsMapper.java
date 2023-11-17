package ru.practicum.ewm.stats.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.StatsRequestDto;
import ru.practicum.ewm.stats.model.Stats;

@Component
public class StatsMapper {
    public Stats mapToStats(StatsRequestDto statsRequestDto) {
        return Stats.builder()
                .app(statsRequestDto.getApp())
                .uri(statsRequestDto.getUri())
                .ip(statsRequestDto.getIp())
                .timestamp(statsRequestDto.getTimestamp())
                .build();
    }
}
