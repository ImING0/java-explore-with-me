package ru.practicum.ewm.statistic.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.StatsServiceClient;
import ru.practicum.ewm.dto.StatsRequestDto;
import ru.practicum.ewm.dto.StatsResponseDto;
import ru.practicum.ewm.statistic.IStatisticService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StatisticService implements IStatisticService {
    private final StatsServiceClient statsServiceClient;

    @Override
    public void addStat(StatsRequestDto statsRequestDto) {
        statsServiceClient.addHit(statsRequestDto);
    }

    @Override
    public List<StatsResponseDto> getStats(LocalDateTime start,
                                           LocalDateTime end,
                                           Set<String> uris,
                                           boolean unique) {
        return statsServiceClient.getStats(start, end, uris, unique)
                .getBody();
    }
}
