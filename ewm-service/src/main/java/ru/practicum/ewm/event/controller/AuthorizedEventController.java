package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.event.EventFullDtoOut;
import ru.practicum.ewm.event.dto.event.EventShortDtoOut;
import ru.practicum.ewm.event.dto.event.EventUserUpdDtoIn;
import ru.practicum.ewm.event.dto.event.NewEventDtoIn;
import ru.practicum.ewm.event.dto.request.EventRequestStatusUpdDtoIn;
import ru.practicum.ewm.event.dto.request.EventRequestStatusUpdDtoOut;
import ru.practicum.ewm.event.service.authorized.IEventAuthorizedService;
import ru.practicum.ewm.request.dto.RequestDtoOut;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class AuthorizedEventController {
    private final IEventAuthorizedService eventService;

    @PostMapping
    public ResponseEntity<EventFullDtoOut> createEvent(@PathVariable("userId") Long userId,
                                                       @RequestBody
                                                       @Valid NewEventDtoIn newEventDtoIn) {
        log.info("Creating event for user with id {} newEventDtoIn {}", userId, newEventDtoIn);

        EventFullDtoOut eventFullDtoOut = eventService.create(newEventDtoIn, userId);

        return new ResponseEntity<>(eventFullDtoOut, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EventShortDtoOut>> getAllEventForCurrentUser(
            @PathVariable(name = "userId", required = true) Long userId,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Getting all events for user with id {}", userId);

        Pageable pageable = PageRequest.of(from / size, size,
                Sort.by(Sort.Direction.DESC, "eventDate"));
        List<EventShortDtoOut> events = eventService.getAllForCurrentUser(userId, pageable);

        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDtoOut> getEventByIdForCurrentUser(
            @PathVariable(name = "userId", required = true) Long userId,
            @PathVariable(name = "eventId", required = true) Long eventId) {
        log.info("Getting event with id {} for user with id {}", eventId, userId);

        EventFullDtoOut eventFullDtoOut = eventService.getByIdForCurrentUser(userId, eventId);

        return ResponseEntity.ok(eventFullDtoOut);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDtoOut> updateEventForCurrentUser(
            @RequestBody @Valid EventUserUpdDtoIn eventUserUpdDtoIn,
            @PathVariable(name = "userId", required = true) Long userId,
            @PathVariable(name = "eventId", required = true) Long eventId) {
        log.info("Updating event with id {} for user with id {}", eventId, userId);

        EventFullDtoOut eventFullDtoOut = eventService.updateForCurrentUser(eventUserUpdDtoIn,
                userId, eventId);

        return ResponseEntity.ok(eventFullDtoOut);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<RequestDtoOut>> getAllRequestByEventIdForCurrentUser(
            @PathVariable(name = "userId", required = true) Long userId,
            @PathVariable(name = "eventId", required = true) Long eventId) {
        log.info("Getting all requests for event with id {} for user with id {} e", eventId,
                userId);

        List<RequestDtoOut> requests = eventService.getAllRequestByEventIdForCurrentUser(userId,
                eventId);

        return ResponseEntity.ok(requests);
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdDtoOut> updateRequestsStatusForCurrentUser(
            @PathVariable(name = "userId", required = true) Long userId,
            @PathVariable(name = "eventId", required = true) Long eventId,
            @RequestBody @Valid EventRequestStatusUpdDtoIn eventRequestStatusUpdDtoIn) {
        log.info(
                "Updating requests status for event with id {} for user with id {} eventRequestStatusUpdDtoIn {}",
                eventId, userId, eventRequestStatusUpdDtoIn);

        EventRequestStatusUpdDtoOut eventRequestStatusUpdDtoOut
                = eventService.updateRequestsStatusForCurrentUser(eventRequestStatusUpdDtoIn,
                userId, eventId);

        return ResponseEntity.ok(eventRequestStatusUpdDtoOut);
    }
}
