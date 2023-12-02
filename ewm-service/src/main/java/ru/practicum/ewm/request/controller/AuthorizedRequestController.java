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

        RequestDtoOut request = requestService.create(userId, eventId);

        return new ResponseEntity<>(request, HttpStatus.CREATED);
    }

    @PatchMapping("/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RequestDtoOut> cancelRequest(
            @PathVariable(name = "userId", required = true) Long userId,
            @PathVariable(name = "requestId", required = true) Long requestId) {
        log.info("cancelRequest: userId={}, requestId={}", userId, requestId);

        RequestDtoOut request = requestService.cancel(userId, requestId);

        return ResponseEntity.ok(request);
    }

    @GetMapping
    public ResponseEntity<List<RequestDtoOut>> getAllForCurrentUser(
            @PathVariable(name = "userId", required = true) Long userId) {
        log.info("getAllForCurrentUser: userId={}", userId);

        List<RequestDtoOut> requests = requestService.getAllForCurrentUser(userId);

        return ResponseEntity.ok(requests);
    }
}
