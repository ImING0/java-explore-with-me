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

    @PatchMapping("/{commId}")
    public ResponseEntity<CommentDtoOut> update(
            @PathVariable(name = "userId", required = true) Long userId,
            @PathVariable(name = "eventId", required = true) Long eventId,
            @PathVariable(name = "commId", required = true) Long commId,
            @RequestBody @Valid CommentDtoIn commentDtoIn) {
        log.info("update: userId={}, eventId={}, commId={}, commentDtoIn={}", userId, eventId,
                commId, commentDtoIn);

        CommentDtoOut commentDtoOut = authorizedCommentService.update(userId, eventId, commId,
                commentDtoIn);

        return ResponseEntity.ok(commentDtoOut);
    }

    /**
     * Закрепить/открепить комментарий
     *
     * @param userId  id инициатора события
     * @param eventId id события
     * @param commId  id комментария
     * @param pinned  true - закрепить, false - открепить
     * @return CommentDtoOut
     */
    @PatchMapping("/{commId}/pin")
    public ResponseEntity<CommentDtoOut> pinComment(
            @PathVariable(name = "userId", required = true) Long userId,
            @PathVariable(name = "eventId", required = true) Long eventId,
            @PathVariable(name = "commId", required = true) Long commId,
            @RequestParam(name = "pinned", required = true) Boolean pinned) {
        log.info("pinComment: userId={}, eventId={}, commId={}", userId, eventId, commId);

        CommentDtoOut commentDtoOut = authorizedCommentService.pin(userId, eventId, commId, pinned);

        return ResponseEntity.ok(commentDtoOut);
    }
}
