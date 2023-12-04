package ru.practicum.ewm.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.comment.model.CommentatorRole;
import ru.practicum.ewm.user.dto.UserShortDtoOut;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDtoOut {
    private Long id;
    private Long eventId;
    private String text;
    private UserShortDtoOut commentator;
    private CommentatorRole commentatorRole;
    private Boolean pinned;
    private LocalDateTime createdOn;
}
