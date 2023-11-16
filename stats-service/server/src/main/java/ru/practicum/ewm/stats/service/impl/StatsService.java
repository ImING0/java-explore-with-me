package ru.practicum.ewm.stats.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.StatsRequestDto;
import ru.practicum.ewm.dto.StatsResponseDto;
import ru.practicum.ewm.stats.mapper.StatsMapper;
import ru.practicum.ewm.stats.model.Stats;
import ru.practicum.ewm.stats.repository.StatsRepository;
import ru.practicum.ewm.stats.service.IStatsService;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StatsService implements IStatsService {
    private final StatsRepository statsRepository;
    private final StatsMapper statsMapper;
    @Override
    public void add(StatsRequestDto requestDto) {
        Stats statsToSave = statsMapper.mapToStats(requestDto);
        statsRepository.save(statsToSave);
    }

    @Override
    public StatsResponseDto get(LocalDateTime start,
                                LocalDateTime end,
                                Set<String> uris,
                                boolean unique) {
        return null;
    }
}
