package ru.practicum.ewm.comment.service.authorized.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.comment.dto.CommentDtoIn;
import ru.practicum.ewm.comment.dto.CommentDtoOut;
import ru.practicum.ewm.comment.mapper.CommentMapper;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.CommentatorRole;
import ru.practicum.ewm.comment.repository.CommentRepository;
import ru.practicum.ewm.comment.service.authorized.IAuthorizedCommentService;
import ru.practicum.ewm.error.ResourceNotFoundException;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthorizedCommentService implements IAuthorizedCommentService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;

    @Override
    public CommentDtoOut create(Long userId,
                                Long eventId,
                                CommentDtoIn commentDtoIn) {
        User commentator = getUserOrThrow(userId);
        Event event = getEventOrThrow(eventId);
        CommentatorRole commentatorRole = getCommentatorRole(commentator, event);
        Comment commentToSave = CommentMapper.toComment(commentDtoIn, commentatorRole, commentator,
                event);
        return CommentMapper.toCommentDtoOut(commentRepository.save(commentToSave));
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
        return event.getInitiator()
                .getId()
                .equals(commentator.getId()) ? CommentatorRole.INITIATOR
                : CommentatorRole.AUTHORIZED;
    }
}
