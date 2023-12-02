package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.StatsRequestDto;
import ru.practicum.ewm.event.dto.event.EventFullDtoOut;
import ru.practicum.ewm.event.dto.event.EventShortDtoOut;
import ru.practicum.ewm.event.service.guest.IEventGuestService;
import ru.practicum.ewm.event.util.SortByForEvent;
import ru.practicum.ewm.statistic.constant.StatsConstants;
import ru.practicum.ewm.util.DateTimeUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/events")
@Slf4j
@RequiredArgsConstructor
public class GuestEventController {
    private final IEventGuestService eventGuestService;

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDtoOut> getById(
            @PathVariable(name = "id", required = true) Long id,
            HttpServletRequest httpServletRequest) {
        log.info("getById: id = {}, userIp = {}", id, httpServletRequest.getRemoteAddr());

        StatsRequestDto statsRequestDto = StatsRequestDto.builder()
                .app(StatsConstants.EWM_MAIN_SERVICE_APP)
                .ip(httpServletRequest.getRemoteAddr())
                .uri(StatsConstants.EVENT_BASE_GUEST_PATH + "/" + id)
                .timestamp(LocalDateTime.now())
                .build();
        EventFullDtoOut eventFullDtoOut = eventGuestService.getById(id, statsRequestDto);

        return ResponseEntity.ok(eventFullDtoOut);
    }

    @GetMapping
    public ResponseEntity<List<EventShortDtoOut>> getAllByParams(
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "categories", required = false) Set<Long> categories,
            @RequestParam(name = "paid", required = false) Boolean paid,
            @RequestParam(name = "rangeStart", required = false)
            @DateTimeFormat(pattern = DateTimeUtil.DATE_TIME_FORMAT) LocalDateTime rangeStart,
            @RequestParam(name = "rangeEnd", required = false)
            @DateTimeFormat(pattern = DateTimeUtil.DATE_TIME_FORMAT) LocalDateTime rangeEnd,
            @RequestParam(name = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(name = "sort", defaultValue = "EVENT_DATE") SortByForEvent sort,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            HttpServletRequest httpServletRequest) {
        log.info("getAllByParams: text = {}, categories = {}, paid = {}, rangeStart = {}, "
                        + "rangeEnd = {}, onlyAvailable = {}, sort = {}, from = {}, size = {}, "
                        + "userIp = {}", text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                from, size, httpServletRequest.getRemoteAddr());

        StatsRequestDto statsRequestDto = StatsRequestDto.builder()
                .app(StatsConstants.EWM_MAIN_SERVICE_APP)
                .ip(httpServletRequest.getRemoteAddr())
                .uri(StatsConstants.EVENT_BASE_GUEST_PATH)
                .timestamp(LocalDateTime.now())
                .build();
        List<EventShortDtoOut> eventsShortDtoOutList = eventGuestService.getAllByParams(text,
                categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size,
                statsRequestDto);

        return ResponseEntity.ok(eventsShortDtoOutList);
    }
}
