package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDtoIn;
import ru.practicum.ewm.comment.dto.CommentDtoOut;
import ru.practicum.ewm.comment.service.authorized.IAuthorizedCommentService;

import javax.validation.Valid;

@RestController
@RequestMapping("/users/{userId}/events/{eventId}/comment")
@Slf4j
@RequiredArgsConstructor
public class AuthorizedCommentController {
    private final IAuthorizedCommentService authorizedCommentService;

    @PostMapping
    public ResponseEntity<CommentDtoOut> createComment(
            @PathVariable(name = "userId", required = true) Long userId,
            @PathVariable(name = "eventId", required = true) Long eventId,
            @RequestBody @Valid CommentDtoIn commentDtoIn) {
        log.info("createComment: userId={}, eventId={}, commentDtoIn={}", userId, eventId,
                commentDtoIn);

        CommentDtoOut commentDtoOut = authorizedCommentService.create(userId, eventId,
                commentDtoIn);

        return new ResponseEntity<>(commentDtoOut, HttpStatus.CREATED);
    }
}
