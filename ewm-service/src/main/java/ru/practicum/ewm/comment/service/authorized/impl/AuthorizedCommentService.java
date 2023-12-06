package ru.practicum.ewm.comment.service.authorized.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comment.dto.CommentDtoIn;
import ru.practicum.ewm.comment.dto.CommentDtoOut;
import ru.practicum.ewm.comment.mapper.CommentMapper;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.CommentState;
import ru.practicum.ewm.comment.model.CommentatorRole;
import ru.practicum.ewm.comment.model.QComment;
import ru.practicum.ewm.comment.repository.CommentRepository;
import ru.practicum.ewm.comment.service.authorized.IAuthorizedCommentService;
import ru.practicum.ewm.error.ForbiddenException;
import ru.practicum.ewm.error.ResourceNotFoundException;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorizedCommentService implements IAuthorizedCommentService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public CommentDtoOut create(Long userId,
                                Long eventId,
                                CommentDtoIn commentDtoIn) {
        User commentator = getUserOrThrow(userId);
        Event event = getEventOrThrow(eventId);

        throwIfEventUnpublishedBeforeCreate(commentator, event);

        CommentatorRole commentatorRole = getCommentatorRole(commentator, event);
        Comment commentToSave = CommentMapper.toComment(commentDtoIn, commentatorRole, commentator,
                event);
        return CommentMapper.toCommentDtoOut(commentRepository.save(commentToSave));
    }

    @Override
    @Transactional
    public CommentDtoOut update(Long userId,
                                Long eventId,
                                Long commId,
                                CommentDtoIn commentDtoIn) {
        User commentator = getUserOrThrow(userId);
        Event event = getEventOrThrow(eventId);
        Comment existingComment = getCommentOrThrow(commId);
        throwIfNotCommentatorBeforeUpdate(commentator, existingComment);
        existingComment.setText(commentDtoIn.getText());
        return CommentMapper.toCommentDtoOut(commentRepository.save(existingComment));
    }

    @Override
    @Transactional
    public CommentDtoOut pin(Long userId,
                             Long eventId,
                             Long commId,
                             Boolean pinned) {
        User initiator = getUserOrThrow(userId);
        Event event = getEventOrThrow(eventId);
        Comment existingComment = getCommentOrThrow(commId);
        checkBeforePinComment(initiator, event, existingComment, pinned);
        return CommentMapper.toCommentDtoOut(commentRepository.save(existingComment));
    }

    @Override
    @Transactional
    public CommentDtoOut delete(Long userId,
                                Long eventId,
                                Long commId) {
        User commentator = getUserOrThrow(userId);
        Event event = getEventOrThrow(eventId);
        Comment existingComment = getCommentOrThrow(commId);
        checkBeforeDelete(commentator, event, existingComment);
        return CommentMapper.toCommentDtoOut(commentRepository.save(existingComment));
    }

    /**
     * Проверка комментария перед созданием, если проверка не пройдена, то выбрасывается исключение.
     *
     * @param commentator комментатор
     * @param event       событие
     */
    private void throwIfEventUnpublishedBeforeCreate(User commentator,
                                                     Event event) {
        // комментатор может создать комментарий только к событию которое опубликовано
        if (!event.getState()
                .equals(EventState.PUBLISHED)) {
            throw new ForbiddenException(String.format(
                    "Commentator with id %d can't create comment to unpublished event with id %d",
                    commentator.getId(), event.getId()));
        }
    }

    /**
     * Проверка комментария перед удалением и изменение состояния удаления.
     *
     * @param commentator     комментатор
     * @param event           событие
     * @param existingComment существующий комментарий
     */
    private void checkBeforeDelete(User commentator,
                                   Event event,
                                   Comment existingComment) {
        // Если комментатор инициатор события и комментарий принадлежит событию, то удаляем
        if (event.getInitiator()
                .getId()
                .equals(commentator.getId()) && event.getId()
                .equals(existingComment.getEvent()
                        .getId())) {
            existingComment.setPinned(false);
            existingComment.setState(CommentState.DELETED_BY_INITIATOR);
            return;
        }

        // комментатор может удалить только свой комментарий
        if (!existingComment.getCommentator()
                .getId()
                .equals(commentator.getId())) {
            throw new ForbiddenException(String.format(
                    "Comment with id %d can be deleted only by commentator with id %d",
                    existingComment.getId(), existingComment.getCommentator()
                            .getId()));
        }

        // Сбрасываем закрепленность.
        existingComment.setPinned(false);
        existingComment.setState(CommentState.DELETED_BY_USER);
    }

    /**
     * Проверка комментария перед закреплением/откреплением и изменение состояния закрепления.
     *
     * @param initiator       инициатор события
     * @param event           событие
     * @param existingComment существующий комментарий
     * @param newPinnedState  новое состояние закрепления
     */
    private void checkBeforePinComment(User initiator,
                                       Event event,
                                       Comment existingComment,
                                       Boolean newPinnedState) {
        // комментарий может закрепить только инициатор события
        if (!event.getInitiator()
                .getId()
                .equals(initiator.getId())) {
            throw new ForbiddenException(
                    String.format("Comment with id %d can be pinned only by initiator with id %d",
                            existingComment.getId(), event.getInitiator()
                                    .getId()));
        }

        // Если комментарий уже закреплен/откреплен и новое состояние ничего не меняет, то ничего
        // не делаем
        if (existingComment.getPinned()
                .equals(newPinnedState)) {
            return;
            // Если комментарий закреплен и новое состояние - открепить, то открепляем
            /*Сноска для себя:
             * true = false из условия выше.*/
        } else if (existingComment.getPinned()
                .equals(true)) {
            existingComment.setPinned(false);
            return;
        }

            /*Проверяем, есть ли у данного события закрепленные комментарии, если новый
            комментарий хочет закрепиться, то открепляем уже закрепленный комментарий и
            прикрепляем новый.*/
        if (newPinnedState) {
            QComment comment = QComment.comment;
            BooleanExpression predicate = comment.isNotNull()
                    .and(comment.event.id.eq(event.getId()))
                    .and(comment.pinned.eq(true));
            Optional<Comment> alreadyPinnedComment = commentRepository.findOne(predicate);
            if (alreadyPinnedComment.isPresent()) {
                Comment commentToUnpin = alreadyPinnedComment.get();
                commentToUnpin.setPinned(false);
                commentRepository.saveAndFlush(commentToUnpin);
            }
            existingComment.setPinned(true);
        }
    }

    /**
     * Проверка комментария перед обновлением, если проверка не пройдена, то выбрасывается исключение.
     *
     * @param commentator комментатор
     * @param comment     комментарий
     */
    private void throwIfNotCommentatorBeforeUpdate(User commentator,
                                                   Comment comment) {
        // комментатор может обновить только свой комментарий
        if (!comment.getCommentator()
                .getId()
                .equals(commentator.getId())) {
            throw new ForbiddenException(String.format(
                    "Comment with id %d can be updated only by commentator with id %d",
                    comment.getId(), comment.getCommentator()
                            .getId()));
        }
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("User with id %d not found", userId)));
    }

    private Event getEventOrThrow(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Event with id %d not found", eventId)));
    }

    public Comment getCommentOrThrow(Long commId) {
        return commentRepository.findById(commId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Comment with id %d not found", commId)));
    }

    /**
     * Получение роли комментатора.
     * Если комментатор - инициатор события, то его роль - {@link CommentatorRole#INITIATOR},
     * иначе - {@link CommentatorRole#AUTHORIZED}.
     *
     * @param commentator комментатор
     * @param event       событие
     * @return роль комментатора
     */
    private CommentatorRole getCommentatorRole(User commentator,
                                               Event event) {
        //@formatter:off
        return event.getInitiator().getId().equals(commentator.getId())
                ? CommentatorRole.INITIATOR
                : CommentatorRole.AUTHORIZED;
        //@formatter:on
    }
}
