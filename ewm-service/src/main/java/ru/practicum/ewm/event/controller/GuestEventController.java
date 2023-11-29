package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.event.Util.SortByForEvent;
import ru.practicum.ewm.event.dto.event.EventShortDtoOut;
import ru.practicum.ewm.event.service.guest.IEventGuestService;
import ru.practicum.ewm.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/events")
@Slf4j
@RequiredArgsConstructor
public class GuestEventController {
    private final IEventGuestService eventGuestService;

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
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("getAllByParams: text = {}, categories = {}, paid = {}, rangeStart = {}, "
                        + "rangeEnd = {}, onlyAvailable = {}, sort = {}, from = {}, size = {}", text,
                categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return ResponseEntity.ok(
                eventGuestService.getAllByParams(text, categories, paid, rangeStart, rangeEnd,
                        onlyAvailable, sort, from, size));
    }
}
