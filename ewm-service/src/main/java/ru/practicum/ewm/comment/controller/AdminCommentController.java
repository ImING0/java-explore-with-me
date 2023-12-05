package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDtoIn;
import ru.practicum.ewm.comment.dto.CommentDtoOut;
import ru.practicum.ewm.comment.service.admin.IAdminCommentService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/events/{eventId}/comment")
@Slf4j
@RequiredArgsConstructor
public class AdminCommentController {
    private final IAdminCommentService adminCommentService;

    @PatchMapping("/{commId}")
    public ResponseEntity<CommentDtoOut> updateComment(
            @RequestBody @Valid CommentDtoIn commentDtoIn,
            @PathVariable(value = "eventId", required = true) Long eventId,
            @PathVariable(value = "commId", required = true) Long commId) {
        log.info("Update comment with id {} for event with id {}, commentDtoIn: {}", commId,
                eventId, commentDtoIn);

        CommentDtoOut commentDtoOut = adminCommentService.update(eventId, commId, commentDtoIn);

        return ResponseEntity.ok(commentDtoOut);
    }

    @DeleteMapping("/{commId}")
    public ResponseEntity<CommentDtoOut> deleteComment(
            @PathVariable(value = "eventId", required = true) Long eventId,
            @PathVariable(value = "commId", required = true) Long commId) {
        log.info("Delete comment with id {} for event with id {}", commId, eventId);

        CommentDtoOut commentDtoOut = adminCommentService.delete(eventId, commId);

        return ResponseEntity.ok(commentDtoOut);
    }
}
