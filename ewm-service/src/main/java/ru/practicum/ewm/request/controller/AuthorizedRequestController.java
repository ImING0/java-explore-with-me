package ru.practicum.ewm.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.RequestDtoOut;
import ru.practicum.ewm.request.service.authorized.IRequestAuthorizedService;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class AuthorizedRequestController {
    private final IRequestAuthorizedService requestService;

    @PostMapping
    public ResponseEntity<RequestDtoOut> createRequest(
            @PathVariable(name = "userId", required = true) Long userId,
            @RequestParam(name = "eventId", required = true) Long eventId) {
        log.info("createRequest: userId={}, eventId={}", userId, eventId);
        return new ResponseEntity<>(requestService.create(userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public void cancelRequest(@PathVariable(name = "userId", required = true) Long userId,
                              @RequestParam(name = "requestId", required = true) Long requestId) {
        log.info("cancelRequest: userId={}, requestId={}", userId, requestId);
        requestService.cancel(userId, requestId);
    }

    @GetMapping
    public ResponseEntity<List<RequestDtoOut>> getAllForCurrentUser(
            @PathVariable(name = "userId", required = true) Long userId) {
        log.info("getAllForCurrentUser: userId={}", userId);
        return ResponseEntity.ok(requestService.getAllForCurrentUser(userId));
    }
}
