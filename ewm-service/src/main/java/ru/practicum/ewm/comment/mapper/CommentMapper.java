package ru.practicum.ewm.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.comment.dto.CommentDtoIn;
import ru.practicum.ewm.comment.dto.CommentDtoOut;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.CommentState;
import ru.practicum.ewm.comment.model.CommentatorRole;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;

/**
 * Маппер комментариев
 */
@UtilityClass
public class CommentMapper {
    /**
     * Маппинг ДТО входящего комментария в комментарий сущности
     *
     * @param commentDtoIn    ДТО входящего комментария
     * @param commentatorRole роль комментатора
     * @param commentator     комментатор
     * @param event           событие
     * @return комментарий
     */
    public Comment toComment(CommentDtoIn commentDtoIn,
                             CommentatorRole commentatorRole,
                             User commentator,
                             Event event) {
        return Comment.builder()
                .text(commentDtoIn.getText())
                .commentatorRole(commentatorRole)
                .state(CommentState.VISIBLE)
                .commentator(commentator)
                .event(event)
                .pinned(false) //при создании комментария он не закреплен
                .build();
    }

    /**
     * Маппинг комментария в ДТО исходящего комментария
     *
     * @param comment комментарий
     * @return ДТО исходящего комментария
     */
    public CommentDtoOut toCommentDtoOut(Comment comment) {
        boolean isVisible = comment.getState()
                .equals(CommentState.VISIBLE);

        return CommentDtoOut.builder()
                .id(comment.getId())
                .eventId(comment.getEvent()
                        .getId())
                .text(isVisible ? comment.getText() : comment.getState()
                        .getDescription())
                .commentator(
                        isVisible ? UserMapper.toUserShortDtoOut(comment.getCommentator()) : null)
                .commentatorRole(isVisible ? comment.getCommentatorRole() : null)
                .pinned(comment.getPinned())
                .createdOn(isVisible ? comment.getCreatedOn() : null)
                .build();
    }
}
