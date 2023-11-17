package ru.practicum.ewm.stats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.StatsRequestDto;
import ru.practicum.ewm.dto.StatsResponseDto;
import ru.practicum.ewm.stats.service.IStatsService;
import ru.practicum.ewm.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final IStatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addHit(@RequestBody StatsRequestDto requestDto) {
        log.info("addHit request: {}", requestDto);
        statsService.add(requestDto);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<StatsResponseDto>> getStats(
            @RequestParam(name = "start", required = true)
            @DateTimeFormat(pattern = DateTimeUtil.DATETIME_FORMAT) LocalDateTime start,
            @RequestParam(name = "end", required = true)
            @DateTimeFormat(pattern = DateTimeUtil.DATETIME_FORMAT) LocalDateTime end,
            @RequestParam(name = "uris", required = false) Set<String> uris,
            @RequestParam(name = "unique", required = false, defaultValue = "false")
            boolean unique) {
        log.info("getStats request: start={}, end={}, uris={}, unique={}", start, end, uris,
                unique);
        return ResponseEntity.ok(statsService.get(start, end, uris, unique));
    }
}
