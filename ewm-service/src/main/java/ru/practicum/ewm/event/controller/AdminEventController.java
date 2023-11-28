package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.event.EventAdminUpdDtoIn;
import ru.practicum.ewm.event.dto.event.EventFullDtoOut;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.service.admin.IEventAdminService;
import ru.practicum.ewm.util.DateTimeUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Slf4j
public class AdminEventController {
    private final IEventAdminService eventAdminService;

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDtoOut> updateEvent(
            @PathVariable(name = "eventId", required = true) Long eventId,
            @RequestBody @Valid EventAdminUpdDtoIn eventAdminUpdDtoIn) {
        log.info("updateEvent: eventId={}, eventAdminUpdDtoIn={}", eventId, eventAdminUpdDtoIn);
        return ResponseEntity.ok(eventAdminService.update(eventAdminUpdDtoIn, eventId));
    }

    @GetMapping
    public ResponseEntity<List<EventFullDtoOut>> getAllByParams(
            @RequestParam(name = "users", required = true) @NotEmpty Set<Long> users,
            @RequestParam(name = "states", required = true) @NotEmpty Set<EventState> states,
            @RequestParam(name = "categories", required = true) @NotEmpty Set<Long> categories,
            @RequestParam(name = "rangeStart", required = true)
            @DateTimeFormat(pattern = DateTimeUtil.DATE_TIME_FORMAT) LocalDateTime rangeStart,
            @RequestParam(name = "rangeEnd", required = true)
            @DateTimeFormat(pattern = DateTimeUtil.DATE_TIME_FORMAT) LocalDateTime rangeEnd,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(name = "size", defaultValue = "10") @PositiveOrZero Integer size) {
        log.info(
                "getAllByParams: users={}, states={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        Pageable pageable = PageRequest.of(from / size, size,
                Sort.by(Sort.Direction.DESC, "eventDate"));
        return ResponseEntity.ok(
                eventAdminService.getAllByParams(users, states, categories, rangeStart, rangeEnd,
                        pageable));
    }
}
