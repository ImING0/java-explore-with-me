package ru.practicum.ewm.stats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.StatsRequestDto;
import ru.practicum.ewm.stats.service.IStatsService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final IStatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addHit(@RequestBody
                       StatsRequestDto requestDto) {
        log.info("addHit request: {}", requestDto);
        statsService.add(requestDto);
    }
}
