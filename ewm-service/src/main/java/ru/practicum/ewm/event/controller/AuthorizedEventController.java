package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDtoOut;
import ru.practicum.ewm.event.dto.NewEventDtoIn;
import ru.practicum.ewm.event.service.authorized.IEventAuthorizedService;

import javax.validation.Valid;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class AuthorizedEventController {
    private final IEventAuthorizedService eventService;

    @PostMapping
    public ResponseEntity<EventFullDtoOut> createNewEvent(@PathVariable("userId") Long userId,
                                                          @RequestBody
                                                          @Valid NewEventDtoIn newEventDtoIn) {
        log.info("Creating new event for user with id {}", userId);
        return new ResponseEntity<>(eventService.create(newEventDtoIn, userId), HttpStatus.CREATED);
    }
}
