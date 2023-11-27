package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventAdminReqDtoIn;
import ru.practicum.ewm.event.dto.EventFullDtoOut;
import ru.practicum.ewm.event.service.admin.IEventAdminService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Slf4j
public class AdminEventController {
    private final IEventAdminService eventAdminService;

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDtoOut> updateEvent(
            @PathVariable(name = "eventId", required = true) Long eventId,
            @RequestBody @Valid EventAdminReqDtoIn eventAdminReqDtoIn) {
        log.info("updateEvent: eventId={}, eventAdminReqDtoIn={}", eventId, eventAdminReqDtoIn);
        return ResponseEntity.ok(eventAdminService.update(eventAdminReqDtoIn, eventId));
    }
}
