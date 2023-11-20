package ru.practicum.ewm.stats.service;

import ru.practicum.ewm.dto.StatsRequestDto;
import ru.practicum.ewm.dto.StatsResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface IStatsService {
    void add(StatsRequestDto requestDto);

    List<StatsResponseDto> get(LocalDateTime start,
                               LocalDateTime end,
                               Set<String> uris,
                               boolean unique);
}
