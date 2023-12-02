package ru.practicum.ewm.stats.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.StatsRequestDto;
import ru.practicum.ewm.dto.StatsResponseDto;
import ru.practicum.ewm.stats.error.BadRequestException;
import ru.practicum.ewm.stats.mapper.StatsMapper;
import ru.practicum.ewm.stats.model.Stats;
import ru.practicum.ewm.stats.repository.StatsRepository;
import ru.practicum.ewm.stats.service.IStatsService;

import java.time.LocalDateTime;
import java.util.List;
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
    public List<StatsResponseDto> get(LocalDateTime start,
                                      LocalDateTime end,
                                      Set<String> uris,
                                      boolean unique) {
        if (start != null && end != null) {
            if (start.isAfter(end)) {
                throw new BadRequestException(
                        String.format("rangeStart %s is after rangeEnd %s", start, end));
            }
        }
        return unique ? statsRepository.findAllStatsUnique(start, end, uris)
                : statsRepository.findAllStats(start, end, uris);
    }
}
