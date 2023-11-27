package ru.practicum.ewm.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.RequestDtoOut;
import ru.practicum.ewm.request.service.authorized.IRequestAuthorizedService;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestController {
    private final IRequestAuthorizedService requestService;

    @PostMapping
    public ResponseEntity<RequestDtoOut> createRequest(
            @PathVariable(name = "userId", required = true) Long userId,
            @RequestParam(name = "eventId", required = true) Long eventId) {
        log.info("createRequest: userId={}, eventId={}", userId, eventId);
        return new ResponseEntity<>(requestService.create(userId, eventId), HttpStatus.CREATED);
    }
}
